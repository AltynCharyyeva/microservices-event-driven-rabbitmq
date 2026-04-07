package mcs_monitoring.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDTO {
    private UUID Id;
    private String username;
    private LocalDateTime timestamp;
    private Double measurementValue;

}
