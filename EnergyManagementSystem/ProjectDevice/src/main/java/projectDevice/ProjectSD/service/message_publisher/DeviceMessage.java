package projectDevice.ProjectSD.service.message_publisher;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class encapsulates the action, deviceId, description,
 * maximumHourlyConsumption and id of the associated user of the device message.
 */
public class DeviceMessage implements Serializable {
    private String action;
    private UUID deviceId;
    private String description;
    private Double maximumHourlyConsumption;
    private UUID clientId;

    public DeviceMessage(String action, UUID deviceId, String description, Double maximumHourlyConsumption, UUID clientId) {
        this.action = action;
        this.deviceId = deviceId;
        this.description = description;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.clientId = clientId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
