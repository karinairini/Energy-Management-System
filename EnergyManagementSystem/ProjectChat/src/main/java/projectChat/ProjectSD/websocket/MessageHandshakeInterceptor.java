package projectChat.ProjectSD.websocket;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Custom WebSocket handshake interceptor that extracts "toUserId" and "fromUserId" parameters from
 * the incoming HTTP request and adds them to the WebSocket session attributes. This enables the
 * WebSocket handler to access these parameters during the WebSocket session.
 */
public class MessageHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * Intercepts the WebSocket handshake request before it is processed.
     * Extracts the "toUserId" and "fromUserId" parameters from the HTTP request and, if present,
     * stores them in the WebSocket session attributes for later access in the WebSocket handler.
     *
     * @param request    the HTTP request during the WebSocket handshake
     * @param response   the HTTP response during the WebSocket handshake
     * @param wsHandler  the WebSocket handler to handle the established connection
     * @param attributes a map of WebSocket session attributes that can be populated for use during the session
     * @return true if the handshake should proceed, false otherwise
     */
    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String toUserId = servletRequest.getParameter("toUserId");
        String fromUserId = servletRequest.getParameter("fromUserId");
        if (toUserId != null && fromUserId != null) {
            attributes.put("toUserId", toUserId);
            attributes.put("fromUserId", fromUserId);
        }
        return true;
    }

    /**
     * Callback method after the WebSocket handshake has completed. This method is typically used
     * for cleanup or additional logging, but it is left empty here as no post-handshake processing
     * is required.
     *
     * @param request   the HTTP request during the WebSocket handshake
     * @param response  the HTTP response during the WebSocket handshake
     * @param wsHandler the WebSocket handler that will manage the connection
     * @param exception any exception raised during the handshake, or null if the handshake was successful
     */
    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception) {
    }
}
