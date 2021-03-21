package model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {


     String firstName;
     String lastName;
     List<Product> purchases;
     Double totalExpenses;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName, Double totalExpenses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalExpenses = totalExpenses;
    }

    @Override
    public String toString() {
        return "\nCustomer{" +
                "\nfirstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", \npurchases=" + purchases +
                ", \ntotalExpenses=" + totalExpenses +
                '}';
    }
}
