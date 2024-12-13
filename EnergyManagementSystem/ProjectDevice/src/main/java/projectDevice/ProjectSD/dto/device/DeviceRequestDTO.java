package projectDevice.ProjectSD.dto.device;

import projectDevice.ProjectSD.dto.user.UserDTO;
import projectDevice.ProjectSD.dto.validator.annotation.ConsumptionLimit;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Data Transfer Object (DTO) for a device request.
 * This DTO encapsulates the description, address, maximumHourlyConsumption and associated user of the device.
 */
public class DeviceRequestDTO implements Serializable {
    private String description;

    @NotNull(message = "The address of a device cannot be missing.")
    private String address;

    @ConsumptionLimit(limit = 20.0)
    private Double maximumHourlyConsumption;

    private UserDTO user;

    public DeviceRequestDTO() {
    }

    public DeviceRequestDTO(String description, String address, Double maximumHourlyConsumption, UserDTO user) {
        this.description = description;
        this.address = address;
        this.maximumHourlyConsumption = maximumHourlyConsumption;
        this.user = user;
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
        DeviceRequestDTO deviceRequestDTO = (DeviceRequestDTO) o;
        return Objects.equals(description, deviceRequestDTO.description) &&
                Objects.equals(address, deviceRequestDTO.address) &&
                Objects.equals(maximumHourlyConsumption, deviceRequestDTO.maximumHourlyConsumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maximumHourlyConsumption);
    }
}
