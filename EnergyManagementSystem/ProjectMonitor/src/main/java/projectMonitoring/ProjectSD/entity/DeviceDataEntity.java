package projectMonitoring.ProjectSD.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a device data entity.
 * This class encapsulates the device's data id, value, timestamp and associated device.
 * It serves as a POJO for mapping data to the "DEVICE_DATA_ENTITY" table in the database.
 */
@Entity
public class DeviceDataEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_device")
    private DeviceEntity device;

    public DeviceDataEntity() {
    }

    public DeviceDataEntity(Double value, Instant timestamp, DeviceEntity device) {
        this.value = value;
        this.timestamp = timestamp;
        this.device = device;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }
}
