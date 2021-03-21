package dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import criterias.Criteria;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import model.Customer;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDto implements InterfaceDto{

    String type = "search";
    Map<Criteria, List<Customer>> results;

}
