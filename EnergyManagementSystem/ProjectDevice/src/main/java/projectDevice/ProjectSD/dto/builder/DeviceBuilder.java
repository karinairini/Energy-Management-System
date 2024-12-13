package projectDevice.ProjectSD.dto.builder;

import projectDevice.ProjectSD.dto.device.DeviceRequestDTO;
import projectDevice.ProjectSD.dto.device.DeviceResponseDTO;
import projectDevice.ProjectSD.entity.DeviceEntity;
import projectDevice.ProjectSD.service.message_publisher.DeviceMessage;

/**
 * DeviceBuilder is a utility class that provides static methods
 * for converting between DeviceEntity, its corresponding DTOs and messages for RabbitMQ.
 */
public class DeviceBuilder {

    private DeviceBuilder() {
    }

    /**
     * Converts a DeviceEntity to a DeviceResponseDTO.
     *
     * @param deviceEntity the DeviceEntity to convert
     * @return the converted DeviceResponseDTO
     */
    public static DeviceResponseDTO toResponseDTO(DeviceEntity deviceEntity) {
        return new DeviceResponseDTO(
                deviceEntity.getId(),
                deviceEntity.getDescription(),
                deviceEntity.getAddress(),
                deviceEntity.getMaximumHourlyConsumption(),
                UserBuilder.toDTO(deviceEntity.getUser())
        );
    }

    /**
     * Converts a DeviceEntity to a DeviceRequestDTO.
     *
     * @param deviceEntity the DeviceEntity to convert
     * @return the converted DeviceRequestDTO
     */
    public static DeviceRequestDTO toRequestDTO(DeviceEntity deviceEntity) {
        return new DeviceRequestDTO(
                deviceEntity.getDescription(),
                deviceEntity.getAddress(),
                deviceEntity.getMaximumHourlyConsumption(),
                UserBuilder.toDTO(deviceEntity.getUser())
        );
    }

    /**
     * Converts a DeviceRequestDTO to a DeviceEntity.
     *
     * @param deviceRequestDTO the DeviceRequestDTO to convert
     * @return the converted DeviceEntity
     */
    public static DeviceEntity toEntity(DeviceRequestDTO deviceRequestDTO) {
        return new DeviceEntity(
                deviceRequestDTO.getDescription(),
                deviceRequestDTO.getAddress(),
                deviceRequestDTO.getMaximumHourlyConsumption(),
                UserBuilder.toEntity(deviceRequestDTO.getUser())
        );
    }

    /**
     * Converts a DeviceEntity and an action to a DeviceMessage.
     *
     * @param action       the action to perform
     * @param deviceEntity the DeviceEntity to convert
     * @return the converted DeviceMessage
     */
    public static DeviceMessage toDeviceMessage(String action, DeviceEntity deviceEntity) {
        return new DeviceMessage(
                action,
                deviceEntity.getId(),
                deviceEntity.getDescription(),
                deviceEntity.getMaximumHourlyConsumption(),
                deviceEntity.getUser().getId()
        );
    }
}
