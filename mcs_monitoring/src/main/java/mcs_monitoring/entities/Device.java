package mcs_monitoring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @Column(name="id")
    private UUID id;

    @Column(name = "maxHourConsumption ")
    private Double energyConsumption;


}
