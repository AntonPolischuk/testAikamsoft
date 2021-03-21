package model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {

     Date date;
     Customer customer;
     List<Product> productList;

    public Orders(Date date, Customer customer, List<Product> productList) {
        this.date = date;
        this.customer = customer;
        this.productList = productList;
    }
}
