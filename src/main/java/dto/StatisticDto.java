package dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonAutoDetect
public class StatisticDto implements InterfaceDto {

     String type = "stat";
     Long totalDays;
     List<CustomerDto> customers;
     Double totalExpenses;
     Double avgExpenses;
}
