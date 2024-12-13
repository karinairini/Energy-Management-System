package projectDevice.ProjectSD.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ messaging components.
 * It defines the necessary beans to configure a RabbitMQ queue, exchange, binding and a message converter for JSON serialization.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.monitor-device-queue}")
    public String queueName;

    @Value("${rabbitmq.exchange.name}")
    public String exchangeName;

    @Value("${rabbitmq.routing.key}")
    public String routingKey;

    /**
     * Creates a durable queue for device monitoring.
     *
     * @return a Queue object configured with the specified queue name
     */
    @Bean
    public Queue deviceQueue() {
        return new Queue(queueName, true);
    }

    /**
     * Creates a binding between the device queue and the topic exchange using the specified routing key.
     *
     * @return a Binding object that links the queue to the exchange
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(deviceQueue())
                .to(exchange())
                .with(routingKey);
    }

    /**
     * Creates a topic exchange for RabbitMQ messaging.
     *
     * @return a TopicExchange object configured with the specified exchange name
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
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

    /**
     * Creates a RabbitAdmin for managing RabbitMQ resources like queues, exchanges and bindings.
     * This will ensure the components are declared in RabbitMQ on startup.
     *
     * @param connectionFactory the connection factory used to create connections to RabbitMQ
     * @return a RabbitAdmin instance configured with the provided connection factory
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.initialize();
        return rabbitAdmin;
    }
}
