package projectMonitoring.ProjectSD.dto.device;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a device.
 * This DTO encapsulates the deviceId, description, maximumHourlyConsumption and id of the associated user of the device.
 */
public class DeviceDTO implements Serializable {
    @NotNull(message = "The id of a device cannot be missing.")
    private UUID deviceID;

    @NotNull(message = "The description of a device cannot be missing.")
    private String description;

    @NotNull(message = "The maximum hourly consumption of a device cannot be missing.")
    private Double maximumHourlyConsumption;

    @NotNull(message = "The id of a user cannot be missing.")
    private UUID userId;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID deviceID, String description, Double maximumHourlyConsumption, UUID userId) {
        this.deviceID = deviceID;
        this.description = description;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.userId = userId;
    }

    public UUID getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(deviceID, deviceDTO.deviceID) &&
                Objects.equals(maximumHourlyConsumption, deviceDTO.maximumHourlyConsumption) &&
                Objects.equals(userId, deviceDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceID, maximumHourlyConsumption, userId);
    }
}
