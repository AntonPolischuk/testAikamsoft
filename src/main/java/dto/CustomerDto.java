package dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import model.Product;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CustomerDto{

    String name;
    List<Product> purchases;
    Double totalExpenses;

}
