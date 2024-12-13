package projectDevice.ProjectSD.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectDevice.ProjectSD.controller.handler.exception.model.ResourceNotFoundException;
import projectDevice.ProjectSD.dto.builder.DeviceBuilder;
import projectDevice.ProjectSD.dto.device.DeviceRequestDTO;
import projectDevice.ProjectSD.dto.device.DeviceResponseDTO;
import projectDevice.ProjectSD.entity.DeviceEntity;
import projectDevice.ProjectSD.entity.UserEntity;
import projectDevice.ProjectSD.repository.DeviceRepository;
import projectDevice.ProjectSD.repository.UserRepository;
import projectDevice.ProjectSD.service.message_publisher.DeviceMessageProducer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing Device entities.
 * This class contains methods for:
 * - Retrieving all device
 * - Retrieving a device by id
 * - Saving a device
 * - Updating a device
 * - Deleting a device
 */
@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class.getName());
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceMessageProducer deviceMessageProducer;

    @Autowired
    public DeviceService(UserRepository userRepository, DeviceRepository deviceRepository, DeviceMessageProducer deviceMessageProducer) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.deviceMessageProducer = deviceMessageProducer;
    }

    /**
     * Retrieves a device by its id.
     *
     * @param id the id of the device to retrieve
     * @return DeviceResponseDTO containing device information
     * @throws ResourceNotFoundException if the device is not found
     */
    public DeviceResponseDTO getDeviceById(UUID id) {
        LOGGER.info("Getting device with id {}.", id);
        DeviceEntity deviceEntity = findDeviceByIdOrThrow(id);

        return DeviceBuilder.toResponseDTO(deviceEntity);
    }

    /**
     * Retrieves all devices by user id.
     *
     * @param userId the id of the user for which to retrieve devices
     * @return List of DeviceResponseDTO containing device information
     */
    public List<DeviceResponseDTO> getDevicesByUserId(UUID userId) {
        LOGGER.info("Getting all devices for user {}.", userId);
        List<DeviceEntity> deviceList = deviceRepository.findByUserId(userId);

        return deviceList.stream()
                .map(DeviceBuilder::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new device to the database associated with a user.
     * Sends a message to RabbitMQ with the device information.
     *
     * @param userId           the id of the user to associate the device with
     * @param deviceRequestDTO the device information to save
     * @return DeviceResponseDTO containing the saved device information
     */
    @Transactional
    public DeviceResponseDTO saveDevice(UUID userId, DeviceRequestDTO deviceRequestDTO) {
        DeviceEntity deviceToBeSaved = DeviceBuilder.toEntity(deviceRequestDTO);

        deviceToBeSaved.setUser(userRepository.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found in database", userId);
            return new ResourceNotFoundException(UserEntity.class.getSimpleName() + " with id: " + userId);
        }));

        DeviceEntity deviceSaved = deviceRepository.save(deviceToBeSaved);
        LOGGER.info("Device with id {} was saved in database to user with id {}.", deviceSaved.getId(), userId);

        deviceMessageProducer.sendMessage(DeviceBuilder.toDeviceMessage("SAVE", deviceSaved));

        return DeviceBuilder.toResponseDTO(deviceSaved);
    }

    /**
     * Updates an existing device in the database.
     * Sends a message to RabbitMQ with the device information.
     *
     * @param id               the id of the device to update
     * @param deviceRequestDTO the new device information
     * @return DeviceResponseDTO containing the updated device information
     * @throws ResourceNotFoundException if the device is not found
     */
    @Transactional
    public DeviceResponseDTO updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {
        findDeviceByIdOrThrow(id);

        DeviceEntity deviceToBeUpdated = DeviceBuilder.toEntity(deviceRequestDTO);
        deviceToBeUpdated.setId(id);

        DeviceEntity deviceUpdated = deviceRepository.save(deviceToBeUpdated);
        LOGGER.info("Device with id {} was updated in database.", id);

        deviceMessageProducer.sendMessage(DeviceBuilder.toDeviceMessage("UPDATE", deviceUpdated));

        return DeviceBuilder.toResponseDTO(deviceUpdated);
    }

    /**
     * Deletes a device from the database.
     * Sends a message to RabbitMQ with the device information.
     *
     * @param id the id of the device to delete
     * @throws ResourceNotFoundException if the device is not found
     */
    @Transactional
    public void deleteDevice(UUID id) {
        DeviceEntity deviceToBeDeleted = findDeviceByIdOrThrow(id);
        deviceRepository.deleteById(id);
        LOGGER.info("Device with id {} was deleted from database.", id);
        deviceMessageProducer.sendMessage(DeviceBuilder.toDeviceMessage("DELETE", deviceToBeDeleted));
    }

    /**
     * Finds a device by its id.
     *
     * @param id the id of the device to find
     * @return DeviceEntity if found
     * @throws ResourceNotFoundException if the device is not found
     */
    private DeviceEntity findDeviceByIdOrThrow(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Device with id {} was not found in database", id);
            return new ResourceNotFoundException(DeviceEntity.class.getSimpleName() + " with id: " + id);
        });
    }
}
