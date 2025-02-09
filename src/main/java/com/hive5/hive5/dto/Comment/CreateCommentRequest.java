package com.hive5.hive5.dto.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    private long post;
    private String content;
    private long parentComment;
}
