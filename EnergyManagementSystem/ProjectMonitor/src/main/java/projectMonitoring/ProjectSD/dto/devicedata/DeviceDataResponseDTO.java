package projectMonitoring.ProjectSD.dto.devicedata;

import projectMonitoring.ProjectSD.dto.device.DeviceDTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Data Transfer Object (DTO) for a device data response.
 * This DTO encapsulates the id, value, timestamp and associated device of the device data.
 */
public class DeviceDataResponseDTO implements Serializable {
    private UUID id;
    private Double value;
    private Instant timestamp;
    private DeviceDTO device;

    public DeviceDataResponseDTO() {
    }

    public DeviceDataResponseDTO(UUID id, Double value, Instant timestamp, DeviceDTO device) {
        this.id = id;
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

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDataResponseDTO deviceDataResponseDTO = (DeviceDataResponseDTO) o;
        return Objects.equals(value, deviceDataResponseDTO.value) &&
                Objects.equals(timestamp, deviceDataResponseDTO.timestamp) &&
                Objects.equals(device, deviceDataResponseDTO.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, timestamp, device);
    }
}
