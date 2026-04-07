package mcs_device.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
public class Person implements Serializable{

    //private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(generator = "uuid2")
    //@GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;


//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "personId", referencedColumnName = "id")
//    private List<Device> devices;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

//    public List<Device> getDevices() {
//        return devices;
//    }
//
//    public void setDevices(List<Device> devices) {
//        this.devices = devices;
//    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
