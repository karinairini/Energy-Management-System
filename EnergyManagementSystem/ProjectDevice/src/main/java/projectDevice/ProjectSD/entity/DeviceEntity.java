package projectDevice.ProjectSD.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a device entity.
 * This class encapsulates the device's id, description, address, maximumHourlyConsumption and associated user.
 * It serves as a POJO for mapping data to the "DEVICE_ENTITY" table in the database.
 */
@Entity
public class DeviceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maximum_hourly_consumption", nullable = false)
    private Double maximumHourlyConsumption;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user")
    private UserEntity user;

    public DeviceEntity() {
    }

    public DeviceEntity(UUID id, String description, String address, Double maximumHourlyConsumption, UserEntity user) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.user = user;
    }

    public DeviceEntity(String description, String address, Double maximumHourlyConsumption, UserEntity user) {
        this.description = description;
        this.address = address;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMaximumHourlyConsumption() {
        return maximumHourlyConsumption;
    }

    public void setMaximumHourlyConsumption(Double maximumHourlyConsumption) {
        this.maximumHourlyConsumption = maximumHourlyConsumption;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
