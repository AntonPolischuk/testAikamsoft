package criterias;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Criteria {

    String lastName;
    String productName;
    Long minTimes;
    Long minExpenses;
    Long maxExpenses;
    Long badCustomers;

}
