package mcs_monitoring.repositories;

import mcs_monitoring.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
