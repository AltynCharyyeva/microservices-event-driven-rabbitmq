package mcs_device.services;

import lombok.Data;
import mcs_device.dtos.DeviceMessage;

import java.io.Serializable;
import java.util.UUID;

@Data
public class DeviceEvent implements Serializable {
    private UUID deviceId;
    private UUID personId;
    private Double energyConsumption;
    private String operation; // Can be "INSERT", "UPDATE", or "DELETE"

    public DeviceEvent(UUID deviceId, UUID personId, Double energyConsumption, String operation) {
        this.deviceId = deviceId;
        this.personId = personId;
        this.energyConsumption = energyConsumption;
        this.operation = operation;
    }

    public DeviceEvent(String operation, DeviceMessage deviceMessage){
        this.deviceId = deviceMessage.getDeviceId();
        this.energyConsumption = deviceMessage.getEnergyConsumption();
        this.operation = operation;
    }

    // Getters and Setters
}

