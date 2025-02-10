package com.hive5.hive5.dto.Comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hive5.hive5.dto.Like.LikeDTO;
import com.hive5.hive5.dto.Post.PostDTO;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.model.Comment;
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
public class CommentDTO {
    private long id;
    private long post;
    private String content;
    private UserDTO user;
    private long parentComment;
    private LocalDateTime createdAt;
    private List<Long> replies;
    private List<LikeDTO> likes;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.post = comment.getPost().getId();
        this.content = comment.getContent();
        this.user = new UserDTO(comment.getUser());
        this.createdAt = comment.getCreatedAt();
        if(comment.getParentComment() != null) {
            this.parentComment = comment.getParentComment().getId();
        } else {
            this.parentComment = 0;
        }

        if(!comment.getReplies().isEmpty()) {
            this.replies = comment.getReplies().stream().map(Comment::getId).toList();
        } else {
            this.replies = new ArrayList<>();
        }

        if(!comment.getLikes().isEmpty()) {
            this.likes = comment.getLikes().stream().map(LikeDTO::new).toList();
        } else this.likes = new ArrayList<>();
    }
}
