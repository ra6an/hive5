package com.hive5.hive5.component;

import com.hive5.hive5.config.CustomHandshakeInterceptor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketEventListener {
    private final ActiveUserService activeUserService;

    public WebSocketEventListener (ActiveUserService activeUserService) {
        this.activeUserService = activeUserService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        System.out.println("TESTTTTTTTTTTT: " + headerAccessor);

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        String userId_ = headerAccessor.getFirstNativeHeader("userId");
        System.out.println("USER ID:        --- " + userId_);

        String userIdStr = (sessionAttributes != null) ? (String)sessionAttributes.get("userId") : null;

        System.out.println("KONEKTOVAN NA SOCKET USER(id): " + userIdStr + " --- sessionID: " + sessionId);

        if(userIdStr != null) {
            try {
                UUID userId = UUID.fromString(userIdStr);
                activeUserService.addUser(userId, sessionId);
                System.out.println("User connected: " + userId + " --- SESSION ID: " + sessionId);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format: " + userIdStr);
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        activeUserService.removeUser(sessionId);
        System.out.println("User disconnected: " + sessionId);
    }
}
