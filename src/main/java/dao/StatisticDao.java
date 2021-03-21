package dao;

import criterias.CriteriaStatForThePeriod;
import dto.CustomerDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import dto.StatisticDto;
import model.Customer;
import model.Product;
import util.ConnectionDB;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticDao {


    CriteriaStatForThePeriod criteriaStatForThePeriod;
    Double totalExpenses=0.0;
    int countCustomer;

    public StatisticDao (CriteriaStatForThePeriod criteriaStatForThePeriod){
        this.criteriaStatForThePeriod = criteriaStatForThePeriod;
    }

    public StatisticDto getStatisticDto() throws IOException, SQLException {
        return makeStatistic();
    }

    private StatisticDto makeStatistic() throws IOException, SQLException {
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setCustomers(convertToCustomerDTO(getCustomerList()));
        statisticDto.setTotalDays(getTotalDays());
        statisticDto.setTotalExpenses(totalExpenses);
        statisticDto.setAvgExpenses(getAvgExpenses());
        return statisticDto;
    }

    private Date getStartDate(){
        return new Date(criteriaStatForThePeriod.getStartDate().getTime());
    }

    private Date getEndDate(){
        return new Date(criteriaStatForThePeriod.getEndDate().getTime());
    }

    private Long getTotalDays(){
        java.util.Date startDate = criteriaStatForThePeriod.getStartDate();
        java.util.Date endDate = criteriaStatForThePeriod.getEndDate();
        return ChronoUnit.DAYS.between(startDate.toInstant(),endDate.toInstant());
    }

    private List<Customer> getCustomerList() throws IOException, SQLException {
        List<Customer> customerList = new LinkedList<>();

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT \"lastName\",\"firstName\",sum(price) FROM orders\n" +
                        "LEFT JOIN customer c ON orders.customer_id = c.id\n" +
                        "LEFT JOIN product p ON orders.product_id = p.id\n" +
                        "WHERE date>=? AND  date<=?\n" +
                        "GROUP BY \"lastName\",\"firstName\"\n" +
                        "ORDER BY sum(price) DESC")) {
            preparedStatement.setDate(1, getStartDate());
            preparedStatement.setDate(2, getEndDate());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                customerList.add(new Customer(rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getDouble("sum")));
                addTotalExpenses(rs.getDouble("sum"));
                countCustomer++;
            }

        } catch (SQLException | IOException e) {
            ExceptionHandler.getInstance().createErrorToOutputFile("Не предвиденная ошибка");
            throw e;
        }
        return fillInCustomersDetails(customerList);
    }


    private List<Customer> fillInCustomersDetails(List<Customer> customerList) throws IOException, SQLException {

        for (Customer customer : customerList) {
            try (Connection connection = ConnectionDB.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT \"lastName\", \"firstName\",\"productName\", sum(price) FROM orders\n" +
                            "LEFT JOIN product p ON p.id = orders.product_id\n" +
                            "LEFT JOIN customer c ON c.id = orders.customer_id\n" +
                            "WHERE date>=? AND  date<=? AND \"firstName\"=? AND \"lastName\"=?\n" +
                            "GROUP BY \"lastName\",\"firstName\",\"productName\"\n" +
                            "ORDER BY sum(price) DESC")) {

                preparedStatement.setDate(1, getStartDate());
                preparedStatement.setDate(2, getEndDate());
                preparedStatement.setString(3, customer.getFirstName());
                preparedStatement.setString(4, customer.getLastName());
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Product> purchases = new LinkedList<>();

                while (resultSet.next()) {
                    purchases.add(new Product(resultSet.getDouble("sum"),
                            resultSet.getString("productName")));
                }
                customer.setPurchases(purchases);
            } catch (SQLException | IOException e) {
                ExceptionHandler.getInstance().createErrorToOutputFile("Не предвиденная ошибка");
                throw e;
            }
        }
        return customerList;
    }


    private List<CustomerDto> convertToCustomerDTO(List<Customer> customers){
        List<CustomerDto> customersDto = new LinkedList<>();
        for(Customer customer:customers){
            customersDto.add(new CustomerDto((customer.getLastName()+ " " + customer.getFirstName()),
                                              customer.getPurchases(),
                                              customer.getTotalExpenses()));
        }
        return customersDto;
    }



    private void addTotalExpenses(Double expenses){
        totalExpenses+=expenses;
    }

    private Double getAvgExpenses() {
        return totalExpenses/countCustomer;
    }

}
