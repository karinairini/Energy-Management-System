package projectMonitoring.ProjectSD.service.message_consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import projectMonitoring.ProjectSD.controller.handler.exception.model.ResourceNotFoundException;
import projectMonitoring.ProjectSD.dto.builder.DeviceBuilder;
import projectMonitoring.ProjectSD.entity.DeviceEntity;
import projectMonitoring.ProjectSD.repository.DeviceRepository;

import java.util.UUID;

/**
 * Service class for consuming device messages from RabbitMQ.
 * This class contains methods for receiving device messages and saving them to the database.
 */
@Service
public class DeviceMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessageConsumer.class);
    private final DeviceRepository deviceRepository;

    public DeviceMessageConsumer(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Receives a device message from RabbitMQ and saves it to the database.
     *
     * @param message the DeviceMessage object to save
     */
    @RabbitListener(queues = "${rabbitmq.queue.monitor-device-queue}")
    public void receiveMessage(DeviceMessage message) {
        LOGGER.info("Received message for action {}: {}", message.getAction(), message);
        switch (message.getAction()) {
            case "SAVE":
                deviceRepository.save(DeviceBuilder.toEntity(message));
                break;
            case "UPDATE":
                updateDevice(message);
                break;
            case "DELETE":
                deviceRepository.delete(DeviceBuilder.toEntity(message));
                break;
            default:
                LOGGER.warn("Unknown action: {}", message.getAction());
        }
    }

    /**
     * Updates a device in the database.
     *
     * @param message the DeviceMessage object to update
     */
    private void updateDevice(DeviceMessage message) {
        findDeviceByIdOrThrow(message.getDeviceId());

        DeviceEntity deviceToBeUpdated = DeviceBuilder.toEntity(message);
        deviceToBeUpdated.setDeviceId(message.getDeviceId());

        deviceRepository.save(deviceToBeUpdated);
    }

    /**
     * Finds a device by its id or throws a ResourceNotFoundException if the device is not found.
     *
     * @param deviceId the id of the device to find
     */
    private void findDeviceByIdOrThrow(UUID deviceId) {
        deviceRepository.findById(deviceId).orElseThrow(() -> {
            LOGGER.error("Device with id {} was not found in database", deviceId);
            return new ResourceNotFoundException(DeviceEntity.class.getSimpleName() + " with id: " + deviceId);
        });
    }
}
