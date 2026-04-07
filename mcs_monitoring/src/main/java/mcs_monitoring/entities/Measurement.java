package mcs_monitoring.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mcs_monitoring.dtos.MeasurementDTO;
import mcs_monitoring.services.LocalDateTimeDeserializer;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Measurement implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id")
    private UUID id;

    @Column(name = "deviceId", nullable = false)
    private UUID deviceId;

    @Column(name = "userId", nullable = false)
    private String username;

    @Column(name = "timestamp", nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    @Column(name = "measurementValue", nullable = false)
    private Double measurementValue;

    public Measurement(UUID deviceId, String username, LocalDateTime timestamp, Double measurementValue){
        this.username = username;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.measurementValue = measurementValue;
    }

}

