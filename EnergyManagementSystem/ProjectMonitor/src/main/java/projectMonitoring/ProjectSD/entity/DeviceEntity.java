package projectMonitoring.ProjectSD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a device entity.
 * This class encapsulates the device's id, description, maximumHourlyConsumption and id of the associated user.
 * It serves as a POJO for mapping data to the "DEVICE_ENTITY" table in the database.
 */
@Entity
public class DeviceEntity implements Serializable {
    @Id
    private UUID deviceId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "maximum_hourly_consumption", nullable = false)
    private Double maximumHourlyConsumption;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public DeviceEntity() {
    }

    public DeviceEntity(UUID deviceId, String description, Double maximumHourlyConsumption, UUID userId) {
        this.deviceId = deviceId;
        this.description = description;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.userId = userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMaximumHourlyConsumption() {
        return maximumHourlyConsumption;
    }

    public void setMaximumHourlyConsumption(Double maximumHourlyConsumption) {
        this.maximumHourlyConsumption = maximumHourlyConsumption;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
