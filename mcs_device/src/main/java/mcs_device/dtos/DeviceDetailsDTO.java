package mcs_device.dtos;

import lombok.Data;
import mcs_device.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class DeviceDetailsDTO {

    private UUID id;
    private UUID personId;
    @NotNull
    private String description;
    @NotNull
    private String address;
    //@AgeLimit(limit = 18)
    private Double energyConsumption;
    private String personName;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO( String description, String address, Double energyConsumption) {
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    public DeviceDetailsDTO(UUID id, String description, String address, Double energyConsumption) {
        this.id = id;
        this.description= description;
        this.address = address;
        this.energyConsumption = energyConsumption;

    }
}
