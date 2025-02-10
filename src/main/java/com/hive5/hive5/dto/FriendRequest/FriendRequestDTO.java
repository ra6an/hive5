package com.hive5.hive5.dto.FriendRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.FriendRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendRequestDTO {
    private long id;
    private UserDTO sender;
    private UserDTO receiver;
    private String status;
    private LocalDateTime createdAt;

    public FriendRequestDTO (FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        this.sender = new UserDTO(friendRequest.getSender());
        this.receiver = new UserDTO(friendRequest.getReceiver());
        this.status = friendRequest.getStatus().name();
        this.createdAt = friendRequest.getCreatedAt();
    }
}
