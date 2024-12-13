package projectChat.ProjectSD.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import projectChat.ProjectSD.security.JwtUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * Handler for WebSocket connections.
 * This handler manages WebSocket connections between users and sends messages between them.
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final HashMap<String, HashMap<String, WebSocketSession>> sessions = new HashMap<>();

    /**
     * Handles the establishment of a new WebSocket connection.
     *
     * @param session the WebSocket session that was established
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String fromUserId = getFromUserId(session);
        String toUserId = getToUserId(session);
        sessions.put(fromUserId, new HashMap<>());
        sessions.get(fromUserId).put(toUserId, session);
    }

    /**
     * Handles the closing of an existing WebSocket connection.
     *
     * @param session the WebSocket session that was closed
     * @param status  the status of the WebSocket connection
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        String fromUserId = getFromUserId(session);
        String toUserId = getToUserId(session);
        sessions.get(fromUserId).remove(toUserId);
    }

    /**
     * Handles incoming text messages from WebSocket connections.
     *
     * @param session the WebSocket session that sent the message
     * @param message the text message that was received
     * @throws Exception if an error occurs while handling the message
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode payload = objectMapper.readTree(message.getPayload());
        if (payload.get("status").asText().equals("auth")) {

            String token = payload.get("token").asText();
            if (!JwtUtil.validateToken(token)) {
                session.close();
            }
        } else {
            String fromUserId = payload.get("fromUserId").asText();
            String toUserId = payload.get("toUserId").asText();
            String userMessage = payload.get("message").asText();
            String status = payload.get("status").asText();

            sendMessage(toUserId, fromUserId, userMessage, status);
        }
    }

    /**
     * Retrieves the user ID of the sender from the WebSocket session.
     *
     * @param session the WebSocket session to retrieve the user ID from
     * @return the user ID of the sender
     */
    private String getFromUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("fromUserId");
    }

    /**
     * Retrieves the user ID of the recipient from the WebSocket session.
     *
     * @param session the WebSocket session to retrieve the user ID from
     * @return the user ID of the recipient
     */
    private String getToUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("toUserId");
    }

    /**
     * Sends a message from one user to another.
     *
     * @param fromUserId the user ID of the sender
     * @param toUserId   the user ID of the recipient
     * @param message    the message to send
     * @param status     the status of the message
     * @throws IOException if an error occurs while sending the message
     */
    public void sendMessage(String fromUserId, String toUserId, String message, String status) throws IOException {
        WebSocketSession session = sessions.get(fromUserId).get(toUserId);
        if (session != null) {
            String jsonMessage = String.format("{\"message\":\"%s\",\"status\":\"%s\"}", message, status);
            session.sendMessage(new TextMessage(jsonMessage));
        }
    }
}
