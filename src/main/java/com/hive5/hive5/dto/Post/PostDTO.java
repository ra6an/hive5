package com.hive5.hive5.dto.Post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.Comment.CommentDTO;
import com.hive5.hive5.dto.Like.LikeDTO;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDTO {
    private long id;
    private String content;
    private UserDTO user;
    private String status;
    private LocalDateTime createdAt;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.status = post.getStatus().name();
        this.user = (post.getUser() != null) ? new UserDTO(post.getUser()) : null;

        if(!post.getComments().isEmpty()) {
            this.comments = post.getComments().stream().map(CommentDTO::new).toList();
        } else {
            this.comments = new ArrayList<>();
        }

        if(!post.getLikes().isEmpty()) {
            this.likes = post.getLikes().stream().map(LikeDTO::new).toList();
        } else this.likes = new ArrayList<>();
    }
}
