package projectMonitoring.ProjectSD.service.message_consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * This class encapsulates the value, timestamp and id of the associated device of the device data message.
 */
public class DeviceDataMessage implements Serializable {

    @JsonProperty("value")
    private Double value;

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
    private Instant timestamp;

    @JsonProperty("deviceId")
    private UUID deviceId;

    @JsonCreator
    public DeviceDataMessage(@JsonProperty("value") Double value,
                             @JsonProperty("timestamp") Instant timestamp,
                             @JsonProperty("deviceId") UUID deviceId) {
        this.value = value;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
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

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }
}
