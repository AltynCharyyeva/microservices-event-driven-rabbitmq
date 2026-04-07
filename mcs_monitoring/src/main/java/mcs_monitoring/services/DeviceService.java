package mcs_monitoring.services;

import mcs_monitoring.entities.Device;
import mcs_monitoring.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> findDevices() {
        return deviceRepository.findAll();
    }


    public Device findDeviceById(UUID id) {
        Optional<Device> optional = deviceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        return optional.get();
    }

    public UUID insert(Device device) {
        device = deviceRepository.save(device);
        return device.getId();
    }

    public UUID update(UUID id, Device device) {
        Optional<Device> optional = deviceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        Device existing = optional.get();
        existing.setEnergyConsumption(device.getEnergyConsumption());

        deviceRepository.save(existing);
        return device.getId();
    }

    public UUID delete(UUID id) {
        Optional<Device> optional = deviceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        return id;
    }
}
