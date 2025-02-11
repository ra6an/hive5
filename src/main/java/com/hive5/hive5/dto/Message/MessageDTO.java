package com.hive5.hive5.dto.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.Message;
import com.hive5.hive5.model.enums.MessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDTO {
    private long id;
    private String content;
    private UserDTO sender;
    private UserDTO receiver;
    private MessageStatus status;
    private LocalDateTime createdAt;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sender = new UserDTO(message.getSender());
        this.receiver = new UserDTO(message.getReceiver());
        this.status = message.getStatus();
        this.createdAt = message.getCreatedAt();
    }
}
