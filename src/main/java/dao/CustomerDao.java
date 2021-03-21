package dao;

import criterias.Criteria;
import dto.SearchDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import model.Customer;
import util.ConnectionDB;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.*;
import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDao {

    final Connection connection = ConnectionDB.getConnection();
    PreparedStatement preparedStatement;
    List<Criteria> criteriaList;
    Map<Criteria, List<Customer>> resultMap = new LinkedHashMap<>();

    public CustomerDao(List<Criteria> criteriaList) throws IOException, SQLException {
        this.criteriaList = criteriaList;
    }

    private void service(List<Criteria> list) throws SQLException {
        for(Criteria criteria:list){
            if(criteria.getLastName()!=null)
                addingToTheResultMap(criteria,getCustomerListByLastName(criteria));
            if(criteria.getMinTimes()!=null)
                addingToTheResultMap(criteria,getCustomerListByMinTimesAndProductName(criteria));
            if(criteria.getMaxExpenses()!=null)
                addingToTheResultMap(criteria,getCustomerListByExpenses(criteria));
            if (criteria.getBadCustomers()!=null)
                addingToTheResultMap(criteria,getCustomerListByBadCustomers(criteria));
        }
    }

    private void addingToTheResultMap(Criteria criteria, List<Customer> customers){
        resultMap.put(criteria,customers);

    }



    public SearchDto getSearchDto() throws IOException, SQLException {
        SearchDto searchDto = new SearchDto();
        searchDto.setResults(getResultMap());
        return searchDto;
    }

    public Map<Criteria,List<Customer>> getResultMap() throws SQLException, IOException {
        try{service(criteriaList);
        } catch (SQLException e){
            ExceptionHandler.getInstance().createErrorToOutputFile("Ошибка базы данных");
            throw e;
        }
        return resultMap;
    }

    private List<Customer> getCustomerListByLastName(Criteria criteria) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("SELECT \"lastName\",\"firstName\" FROM customer WHERE \"lastName\"=?");
        preparedStatement.setString(1,criteria.getLastName());
        ResultSet rs = preparedStatement.executeQuery();
        return  fillCustomerList(rs);
    }

    private List<Customer> getCustomerListByMinTimesAndProductName(Criteria criteria) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("SELECT \"lastName\",\"firstName\", count(\"productName\") FROM orders\n" +
                        "LEFT JOIN customer c on c.id = orders.customer_id\n" +
                        "LEFT JOIN product p on orders.product_id = p.id\n" +
                        "WHERE \"productName\"=?\n" +
                         "GROUP BY \"lastName\",\"firstName\"\n" +
                        "HAVING count(\"productName\")>=?");

        preparedStatement.setString(1,criteria.getProductName());
        preparedStatement.setLong(2,criteria.getMinTimes());
        ResultSet rs = preparedStatement.executeQuery();
        return  fillCustomerList(rs);
    }

    private List<Customer> getCustomerListByExpenses(Criteria criteria) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("SELECT \"lastName\",\"firstName\",sum(price) FROM orders\n" +
                        "LEFT JOIN customer c on c.id = orders.customer_id\n" +
                        "LEFT JOIN product p on p.id = orders.product_id\n" +
                        "GROUP BY \"lastName\",\"firstName\"\n" +
                        "HAVING (sum(price)<? and sum(price)>?);");

        preparedStatement.setLong(1,criteria.getMaxExpenses());
        preparedStatement.setLong(2,criteria.getMinExpenses());
        ResultSet rs = preparedStatement.executeQuery();
        return  fillCustomerList(rs);
    }

    private List<Customer> getCustomerListByBadCustomers(Criteria criteria) throws SQLException {
        preparedStatement = connection.prepareStatement
                ("SELECT \"lastName\",\"firstName\",count(\"productName\") FROM orders\n" +
                        "LEFT OUTER JOIN customer c on c.id = orders.customer_id\n" +
                        "LEFT JOIN product p on p.id = orders.product_id\n" +
                        "GROUP BY \"lastName\",\"firstName\"\n" +
                        "ORDER BY count(\"productName\") LIMIT ?");

        preparedStatement.setLong(1,criteria.getBadCustomers());
        ResultSet rs = preparedStatement.executeQuery();
        return  fillCustomerList(rs);
    }

    private List<Customer> fillCustomerList(ResultSet resultSet) throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()){
            customerList.add(new Customer(resultSet.getString("firstName"),
                                          resultSet.getString("lastName")));
        }
        return customerList;
    }
}
