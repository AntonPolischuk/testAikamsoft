package model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {



     String productName;
     Double price;

    public Product(Double price, String productName) {
        this.price = price;
        this.productName = productName;
    }


}
