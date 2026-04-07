package mcs_device.dtos.builders;

import mcs_device.dtos.DeviceDTO;
import mcs_device.dtos.DeviceDetailsDTO;
import mcs_device.dtos.OnlyDeviceDTO;
import mcs_device.entities.Device;


public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getDescription(), device.getAddress(), device.getEnergyConsumption());
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getEnergyConsumption());
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        return new Device(
                deviceDetailsDTO.getDescription(),
                deviceDetailsDTO.getAddress(),
                deviceDetailsDTO.getEnergyConsumption());
    }

    public static DeviceDetailsDTO toOnlyDeviceDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getEnergyConsumption());
    }

    public static Device toOnlyDeviceEntity(OnlyDeviceDTO onlyDeviceDTO) {
        return new Device(
                onlyDeviceDTO.getDescription(),
                onlyDeviceDTO.getAddress(),
                onlyDeviceDTO.getEnergyConsumption());
    }
}
