package projectMonitoring.ProjectSD.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import projectMonitoring.ProjectSD.websocket.UserHandshakeInterceptor;
import projectMonitoring.ProjectSD.websocket.UserNotificationWebSocketHandler;

/**
 * WebSocket configuration class for setting up WebSocket endpoints and handlers.
 * This configuration enables WebSocket support and registers WebSocket handlers
 * for managing user notifications.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final UserNotificationWebSocketHandler userNotificationWebSocketHandler;

    public WebSocketConfig(UserNotificationWebSocketHandler userNotificationWebSocketHandler) {
        this.userNotificationWebSocketHandler = userNotificationWebSocketHandler;
    }

    /**
     * Registers WebSocket handlers with specific endpoints and configurations.
     * This method sets up a WebSocket endpoint for notifications and applies a custom
     * handshake interceptor for user identification.
     *
     * @param registry the WebSocketHandlerRegistry used to register WebSocket handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(userNotificationWebSocketHandler, "/ws/notifications")
                .addInterceptors(new UserHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:4200");
    }
}
