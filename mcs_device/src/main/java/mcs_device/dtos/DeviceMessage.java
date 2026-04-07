package mcs_device.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMessage {
    private UUID deviceId;
    private Double energyConsumption;

    public DeviceMessage(UUID deviceId, double energyConsumption) {
        this.deviceId = deviceId;
        this.energyConsumption = energyConsumption;
    }

    @Override
    public String toString() {
        return "DeviceMessage{" +
                "deviceId=" + deviceId +
                ", energyConsumption=" + energyConsumption +
                '}';
    }
}
