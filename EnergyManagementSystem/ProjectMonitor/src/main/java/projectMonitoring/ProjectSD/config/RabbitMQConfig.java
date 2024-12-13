package projectMonitoring.ProjectSD.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ messaging components.
 * It defines the necessary beans to configure RabbitMQ queues for device and sensor monitoring,
 * along with a message converter for JSON serialization.
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.monitor-device-queue}")
    public String queueDeviceName;

    @Value("${rabbitmq.queue.monitor-sensor-queue}")
    public String queueSensorName;

    /**
     * Creates a durable queue for device monitoring.
     *
     * @return a Queue object configured with the specified device queue name
     */
    @Bean
    public Queue deviceQueue() {
        return new Queue(queueDeviceName, true);
    }

    /**
     * Creates a durable queue for sensor monitoring.
     *
     * @return a Queue object configured with the specified sensor queue name
     */
    @Bean
    public Queue sensorQueue() {
        return new Queue(queueSensorName, true);
    }

    /**
     * Configures a message converter for serializing and deserializing messages as JSON using Jackson.
     *
     * @return a Jackson2JsonMessageConverter instance for JSON conversion
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a RabbitTemplate for sending and receiving messages with RabbitMQ.
     * This template is configured with a connection factory and a message converter.
     *
     * @param connectionFactory the connection factory used to create connections to RabbitMQ
     * @return a RabbitTemplate instance configured with the provided connection factory and message converter
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
