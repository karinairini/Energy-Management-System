package projectDevice.ProjectSD.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectDevice.ProjectSD.controller.handler.exception.model.ResourceNotFoundException;
import projectDevice.ProjectSD.dto.builder.DeviceBuilder;
import projectDevice.ProjectSD.dto.builder.UserBuilder;
import projectDevice.ProjectSD.dto.user.UserDTO;
import projectDevice.ProjectSD.entity.UserEntity;
import projectDevice.ProjectSD.repository.DeviceRepository;
import projectDevice.ProjectSD.repository.UserRepository;
import projectDevice.ProjectSD.service.message_publisher.DeviceMessageProducer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing User entities.
 * This class contains methods for:
 * - Retrieving all users
 * - Saving a user
 * - Updating a user
 * - Deleting a user
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceMessageProducer deviceMessageProducer;

    @Autowired
    public UserService(UserRepository userRepository, DeviceRepository deviceRepository, DeviceMessageProducer deviceMessageProducer) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.deviceMessageProducer = deviceMessageProducer;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return list of UserDTO containing user information
     */
    public List<UserDTO> getUsers() {
        LOGGER.info("Getting all users.");
        List<UserEntity> userList = userRepository.findAll();

        return userList.stream()
                .map(UserBuilder::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new user to the database.
     *
     * @param userDTO the user information to save
     * @return UserDTO containing the saved user information
     */
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity userToBeSaved = UserBuilder.toEntity(userDTO);

        UserEntity userSaved = userRepository.save(userToBeSaved);
        LOGGER.info("Person with id {} was saved in database.", userSaved.getId());

        return UserBuilder.toDTO(userSaved);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id      the id of the user to update
     * @param userDTO the new user information
     * @return UserDTO containing the updated user information
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        findUserByIdOrThrow(id);

        UserEntity userToBeUpdated = UserBuilder.toEntity(userDTO);
        userToBeUpdated.setId(id);

        UserEntity userUpdated = userRepository.save(userToBeUpdated);
        LOGGER.info("Person with id {} was updated in database.", id);

        return UserBuilder.toDTO(userUpdated);
    }

    /**
     * Deletes a user by its id from the database.
     * Sends a message to RabbitMQ to delete all devices associated with the user.
     *
     * @param id the id of the user to delete
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public void deleteUser(UUID id) {
        findUserByIdOrThrow(id);
        deviceRepository.findByUserId(id).forEach(deviceEntity -> {
            deviceMessageProducer.sendMessage(DeviceBuilder.toDeviceMessage("DELETE", deviceEntity));
        });
        userRepository.deleteById(id);
        LOGGER.info("Person with id {} was deleted from database.", id);
    }

    /**
     * Finds a user by its id.
     *
     * @param id the id of the user to find
     * @throws ResourceNotFoundException if the user is not found
     */
    private void findUserByIdOrThrow(UUID id) {
        userRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found in database.", id);
            return new ResourceNotFoundException(UserEntity.class.getSimpleName() + " with id: " + id);
        });
    }
}
