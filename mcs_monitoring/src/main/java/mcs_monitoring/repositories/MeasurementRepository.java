package mcs_monitoring.repositories;

import mcs_monitoring.entities.Measurement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    List<Measurement> findByDeviceId(UUID deviceId);

    @Query("SELECT m FROM Measurement m WHERE m.deviceId = :deviceId")
    List<Measurement> findByDeviceId(@Param("deviceId") UUID deviceId, Pageable pageable);


}
