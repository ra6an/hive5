package com.hive5.hive5.dto.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.enums.MessageStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatDTO {
    private UserDTO participant;
    private List<MessageDTO> messages;
    private boolean haveUnreadMessages;

    public ChatDTO(UserDTO user, List<MessageDTO> messages) {
        this.participant = user;
        this.messages = messages;
        this.haveUnreadMessages = messages.stream()
                .anyMatch(m -> m.getSender().getId().equals(user.getId()) && m.getStatus() == MessageStatus.SENT);
    }
}
