package com.hive5.hive5.service;

import com.hive5.hive5.component.ActiveUserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class WSNotificationService {
    private final ActiveUserService activeUserService;
    private final SimpMessagingTemplate messagingTemplate;

    public WSNotificationService(ActiveUserService activeUserService, SimpMessagingTemplate messagingTemplate) {
        this.activeUserService = activeUserService;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendDataViaWS(UUID receiverId, Map<String, Object> data, WSType type) {
//        Set<String> sessionIds = activeUserService.getUserSessionId(receiverId);

        data.put("wsType", type);

        String destination = "/topic/user-events/" + receiverId;
        messagingTemplate.convertAndSend(destination, data);
//        for(String sessionId : sessionIds) {
//            String destination = "/topic/user-events/" + sessionId;
//            messagingTemplate.convertAndSend(destination, data);
//        }

//        if(sessionIds.isEmpty()) {
//            System.out.println("User " + receiverId + " is offline.");
//            return;
//        }

//        System.out.println("Send notification to: " + receiverId + "(session: " + sessionIds + ")");
    }
}
