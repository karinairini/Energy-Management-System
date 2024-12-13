package projectChat.ProjectSD.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket configuration class for setting up WebSocket endpoints and handlers.
 * This class registers a WebSocket handler for chat messages and applies a handshake
 * interceptor for user identification.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * Registers WebSocket handlers with specific endpoints and configurations.
     * This method sets up a WebSocket endpoint for chat messages and applies
     * a custom handshake interceptor for user identification.
     *
     * @param registry the WebSocketHandlerRegistry used to register WebSocket handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat")
                .addInterceptors(new MessageHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
