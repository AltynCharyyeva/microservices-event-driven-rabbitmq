package mcs_device.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
public class Device  implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "energyConsumption", nullable = false)
    private Double energyConsumption;

    //@ManyToOne( optional = true)
    @Column(name="person_id")
    private UUID personId;

    public Device() {
    }

    public Device(String description, String address, Double energyConsumption) {
        //this.personId = personId;
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

}
