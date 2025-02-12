package com.hive5.hive5.dto.Notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.Notification;
import com.hive5.hive5.model.enums.NotificationType;
import com.hive5.hive5.model.enums.TargetType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDTO {
    private long id;
    private UserDTO sender;
    private UUID receiver;
    private NotificationType type;
    private TargetType targetType;
    private Long targetId;
    private Long secondaryTargetId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.sender = new UserDTO(notification.getSender());
        this.receiver = notification.getReceiver().getId();
        this.type = notification.getType();
        this.targetType = notification.getTargetType();
        this.targetId = notification.getTargetId();
        this.secondaryTargetId = notification.getSecondaryTargetId();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}
