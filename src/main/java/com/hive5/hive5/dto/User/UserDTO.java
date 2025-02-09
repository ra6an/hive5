package com.hive5.hive5.dto.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.model.enums.Role;
import com.hive5.hive5.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole();
    }
}
