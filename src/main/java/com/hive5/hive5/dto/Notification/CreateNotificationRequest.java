package com.hive5.hive5.dto.Notification;

import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.NotificationType;
import com.hive5.hive5.model.enums.TargetType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateNotificationRequest {
    private User sender;
    private User receiver;
    private NotificationType type;
    private TargetType targetType;
    private Long targetId;
    private Long secondaryTargetId;
//    private boolean isRead;
}
