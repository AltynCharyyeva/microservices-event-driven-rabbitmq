package mcs_device.dtos;

import java.util.UUID;

public class DevicePersonAssociationDTO {

    private UUID personId;
    private UUID deviceId;

    public DevicePersonAssociationDTO() {}

    public DevicePersonAssociationDTO(UUID personId, UUID deviceId) {
        this.personId = personId;
        this.deviceId = deviceId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }
}
