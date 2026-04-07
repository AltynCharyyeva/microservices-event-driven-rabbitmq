package mcs_monitoring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEventDTO {

    @NotNull
    private UUID deviceId;

    @NotNull
    private Double energyConsumption;

    @NotNull
    private String operation;
}
