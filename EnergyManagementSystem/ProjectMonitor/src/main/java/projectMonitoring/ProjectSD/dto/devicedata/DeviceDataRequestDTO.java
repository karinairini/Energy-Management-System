package projectMonitoring.ProjectSD.dto.devicedata;

import projectMonitoring.ProjectSD.dto.device.DeviceDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a Data Transfer Object (DTO) for a device data request.
 * This DTO encapsulates the value, timestamp and associated device of the device data.
 */
public class DeviceDataRequestDTO implements Serializable {
    @NotNull(message = "The value of a device data cannot be missing.")
    private Double value;

    @NotNull(message = "The timestamp of a device data cannot be missing.")
    private Instant timestamp;

    @NotNull(message = "The device of a device data cannot be missing.")
    private DeviceDTO device;

    public DeviceDataRequestDTO() {
    }

    public DeviceDataRequestDTO(Double value, Instant timestamp, DeviceDTO device) {
        this.value = value;
        this.timestamp = timestamp;
        this.device = device;
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

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDataRequestDTO deviceDataRequestDTO = (DeviceDataRequestDTO) o;
        return Objects.equals(value, deviceDataRequestDTO.value) &&
                Objects.equals(timestamp, deviceDataRequestDTO.timestamp) &&
                Objects.equals(device, deviceDataRequestDTO.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, timestamp, device);
    }

}
