package projectMonitoring.ProjectSD.service.message_consumer;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import projectMonitoring.ProjectSD.controller.handler.exception.model.ResourceNotFoundException;
import projectMonitoring.ProjectSD.dto.builder.DeviceDataBuilder;
import projectMonitoring.ProjectSD.entity.DeviceDataEntity;
import projectMonitoring.ProjectSD.entity.DeviceEntity;
import projectMonitoring.ProjectSD.repository.DeviceDataRepository;
import projectMonitoring.ProjectSD.repository.DeviceRepository;
import projectMonitoring.ProjectSD.websocket.UserNotificationWebSocketHandler;

import java.io.IOException;
import java.util.UUID;

/**
 * Service class for consuming device data messages from RabbitMQ.
 * This class contains methods for receiving device data messages and saving them to the database.
 * If the device's consumption exceeds the maximum hourly consumption, a notification is sent to the user.
 */
@Service
public class DeviceDataMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataMessageConsumer.class);
    private final DeviceRepository deviceRepository;
    private final DeviceDataRepository deviceDataRepository;
    private final UserNotificationWebSocketHandler notificationWebSocketHandler;

    public DeviceDataMessageConsumer(DeviceRepository deviceRepository,
                                     DeviceDataRepository deviceDataRepository,
                                     UserNotificationWebSocketHandler notificationWebSocketHandler) {
        this.deviceRepository = deviceRepository;
        this.deviceDataRepository = deviceDataRepository;
        this.notificationWebSocketHandler = notificationWebSocketHandler;
    }

    /**
     * Receives a device data message from RabbitMQ and saves it to the database.
     * If the device's consumption exceeds the maximum hourly consumption, a notification is sent to the user.
     *
     * @param deviceDataMessage the DeviceDataMessage object to save
     */
    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.monitor-sensor-queue}")
    public void receiveMessage(DeviceDataMessage deviceDataMessage) throws IOException {
        LOGGER.info("Received message with data: {}", deviceDataMessage);

        DeviceEntity deviceEntity = findDeviceByIdOrThrow(deviceDataMessage.getDeviceId());
        DeviceDataEntity deviceDataEntity = DeviceDataBuilder.toEntity(deviceDataMessage, deviceEntity);

        if (deviceDataMessage.getValue() > deviceEntity.getMaximumHourlyConsumption()) {
            sendConsumptionAlert(deviceEntity, deviceDataMessage);
        }

        deviceDataRepository.save(deviceDataEntity);
    }

    /**
     * Finds a device by its id or throws a ResourceNotFoundException if the device is not found.
     *
     * @param deviceId the id of the device to find
     */
    private DeviceEntity findDeviceByIdOrThrow(UUID deviceId) {
        return deviceRepository.findById(deviceId).orElseThrow(() -> {
            LOGGER.error("Device with id {} was not found in database", deviceId);
            return new ResourceNotFoundException(DeviceEntity.class.getSimpleName() + " with id: " + deviceId);
        });
    }

    /**
     * Sends a consumption alert notification to the user through a WebSocket if the device's consumption is exceeded.
     *
     * @param deviceEntity      the device entity associated with the user and device details
     * @param deviceDataMessage the message containing the device's latest data reading
     * @throws IOException if there is an error sending the WebSocket message
     */
    private void sendConsumptionAlert(DeviceEntity deviceEntity, DeviceDataMessage deviceDataMessage) throws IOException {
        String notificationMessage = buildNotificationMessage(deviceEntity, deviceDataMessage);

        String userId = String.valueOf(deviceEntity.getUserId());
        TextMessage textMessage = new TextMessage(notificationMessage);
        notificationWebSocketHandler.sendMessage(userId, textMessage);
    }

    /**
     * Constructs a JSON-formatted notification message string for alerting the user.
     *
     * @param deviceEntity      the device entity containing the device and user information
     * @param deviceDataMessage the data message with the current consumption value
     * @return a JSON string containing user id, device description, alert type, and consumption values
     */
    private String buildNotificationMessage(DeviceEntity deviceEntity, DeviceDataMessage deviceDataMessage) {
        return String.format(
                "{\"userId\":\"%s\", \"description\":\"%s\", \"alert\":\"Consumption limit exceeded\", " +
                        "\"value\":\"%.2f\", \"maximumHourlyConsumption\":\"%.2f\"}",
                deviceEntity.getUserId(),
                deviceEntity.getDescription(),
                deviceDataMessage.getValue(),
                deviceEntity.getMaximumHourlyConsumption()
        );
    }
}
