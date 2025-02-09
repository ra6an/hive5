package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.dto.Comment.CreateCommentRequest;
import com.hive5.hive5.repository.CommentRepository;
import com.hive5.hive5.repository.UserRepository;
import com.hive5.hive5.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createComment(@RequestBody CreateCommentRequest createCommentRequest, Principal principal) {
        Map<String, Object> data = commentService.createComment(createCommentRequest, principal);

        ApiResponse response = new ApiResponse();
        response.setData(data);
        response.setMessage("Successfully commented.");
        response.setStatus("OK");
        response.setStatusCode(200);

        return ResponseEntity.ok(response);
    }
}
