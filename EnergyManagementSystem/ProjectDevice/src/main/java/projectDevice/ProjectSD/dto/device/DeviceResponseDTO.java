package projectDevice.ProjectSD.dto.device;

import projectDevice.ProjectSD.dto.user.UserDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Data Transfer Object (DTO) for a device response.
 * This DTO encapsulates the id, description, address, maximumHourlyConsumption and associated user of the device.
 */
public class DeviceResponseDTO implements Serializable {
    private UUID id;
    private String description;
    private String address;
    private Double maximumHourlyConsumption;
    private UserDTO user;

    public DeviceResponseDTO() {
    }

    public DeviceResponseDTO(UUID id, String description, String address, Double maximumHourlyConsumption, UserDTO user) {
        this.id = id;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceResponseDTO deviceResponseDTO = (DeviceResponseDTO) o;
        return Objects.equals(description, deviceResponseDTO.description) &&
                Objects.equals(address, deviceResponseDTO.address) &&
                Objects.equals(maximumHourlyConsumption, deviceResponseDTO.maximumHourlyConsumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maximumHourlyConsumption);
    }
}
