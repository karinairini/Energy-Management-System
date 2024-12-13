package projectMonitoring.ProjectSD.websocket;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for managing user-specific WebSocket sessions and sending
 * notifications. Each connected user's WebSocket session is stored and can be
 * used to send targeted messages.
 */
@Component
public class UserNotificationWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * Called after the WebSocket connection is successfully established. This method
     * retrieves the user id from the session attributes and stores the WebSocket session in a map.
     *
     * @param session the WebSocket session that was established
     * @throws Exception if an error occurs during connection setup
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");

        if (userId != null) {
            sessions.put(userId, session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    /**
     * Called when a WebSocket connection is closed. This method removes the WebSocket
     * session from the sessions map.
     *
     * @param session the WebSocket session that was closed
     * @param status  the status indicating why the WebSocket connection was closed
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);
    }

    /**
     * Sends a message to the specified user if their WebSocket session is active and open.
     * The message is only sent if the session exists and is currently open.
     *
     * @param userId  the id of the user to whom the message should be sent
     * @param message the message to send to the user's WebSocket session
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void sendMessage(String userId, TextMessage message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
}
