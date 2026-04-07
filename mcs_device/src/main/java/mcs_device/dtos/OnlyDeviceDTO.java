package mcs_device.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class OnlyDeviceDTO {

    private UUID id;

    @NotNull
    private String description;
    @NotNull
    private String address;
    //@AgeLimit(limit = 18)
    private Double energyConsumption;

    public OnlyDeviceDTO() {
    }

    public OnlyDeviceDTO( String description, String address, Double energyConsumption) {
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    public OnlyDeviceDTO(UUID id, String description, String address, Double energyConsumption) {
        this.id = id;
        this.description= description;
        this.address = address;
        this.energyConsumption = energyConsumption;

    }
}
