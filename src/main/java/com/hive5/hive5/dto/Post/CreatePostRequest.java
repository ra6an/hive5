package com.hive5.hive5.dto.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    private String content;
    private String status;
}
