package mcs_device.dtos;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class DeviceDTO {
    private UUID id;
    private String description;
    private String address;
    private Double energyConsumption;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String description, String address, Double energyConsumption) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return energyConsumption == deviceDTO.energyConsumption &&
                Objects.equals(description, deviceDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, energyConsumption);
    }
}
