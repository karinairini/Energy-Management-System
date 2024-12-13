package projectDevice.ProjectSD.service.message_publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for publishing device messages to RabbitMQ.
 * This class contains a method for sending device messages to the RabbitMQ exchange.
 */
@Service
public class DeviceMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessageProducer.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    public String exchangeName;

    @Value("${rabbitmq.routing.key}")
    public String routingKey;

    @Autowired
    public DeviceMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends a device message to the RabbitMQ exchange.
     *
     * @param deviceMessage the DeviceMessage object to send
     */
    public void sendMessage(DeviceMessage deviceMessage) {
        LOGGER.info("Publishing message for action {}", deviceMessage.getAction());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, deviceMessage);
    }
}
