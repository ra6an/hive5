package com.hive5.hive5.service;

import com.hive5.hive5.dto.Notification.CreateNotificationRequest;
import com.hive5.hive5.dto.Notification.NotificationDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.Notification;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.NotificationRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Map<String, Object> createNotification(CreateNotificationRequest createNotificationRequest) {
        Notification notification = new Notification();

        notification.setSender(createNotificationRequest.getSender());
        notification.setReceiver(createNotificationRequest.getReceiver());
        notification.setType(createNotificationRequest.getType());
        notification.setTargetType(createNotificationRequest.getTargetType());
        notification.setTargetId(createNotificationRequest.getTargetId());
        notification.setSecondaryTargetId(createNotificationRequest.getSecondaryTargetId());

        return createData(notification);
    }

    public Map<String, Object> updateNotificationIsReadStatusToSeen(@PathVariable Long notificationId, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException("Bad Request!", "There is no notification with that ID.", 404));

        if(!notification.getReceiver().getId().equals(user.getId()))
            throw new CustomException("Unauthorized!", "You are not authorized", 401);

        notification.setRead(true);

        return createData(notification);
    }

    private Map<String, Object> createData(Notification notification) {
        notificationRepository.save(notification);

        NotificationDTO notDTO = new NotificationDTO(notification);

        Map<String, Object> data = new HashMap<>();
        data.put("data", notDTO);

        return data;
    }
}
