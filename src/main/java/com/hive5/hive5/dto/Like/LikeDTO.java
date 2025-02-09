package com.hive5.hive5.dto.Like;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.Like;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeDTO {
    private long id;
    private UserDTO user;
    private long post;
    private long comment;

    public LikeDTO(Like like) {
        this.id = like.getId();
        this.user = new UserDTO(like.getUser());
        if(like.getPost() != null) {
            this.post = like.getPost().getId();
        } else this.post = 0;

        if(like.getComment() != null) {
            this.comment = like.getComment().getId();
        } else this.comment = 0;
    }
}
