package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse> likeComment(@PathVariable long commentId, Principal principal) {
        Map<String, Object> data = likeService.like(commentId, 0, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("Successfully liked comment.");
        response.setData(data);
        response.setStatus("OK");
        response.setStatusCode(200);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> likePost(@PathVariable long postId, Principal principal) {
        Map<String, Object> data = likeService.like(0, postId, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("Successfully liked post.");
        response.setData(data);
        response.setStatus("OK");
        response.setStatusCode(200);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/dislike/comment/{commentId}")
    public ResponseEntity<ApiResponse> dislikeComment(@PathVariable long commentId, Principal principal) {
        Map<String, Object> data = likeService.dislike(0, commentId, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("Successfully disliked.");
        response.setData(data);
        response.setStatus("OK");
        response.setStatusCode(200);

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/dislike/post/{postId}")
    public ResponseEntity<ApiResponse> dislikePost(@PathVariable long postId, Principal principal) {
        Map<String, Object> data = likeService.dislike(postId, 0, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("Successfully disliked.");
        response.setData(data);
        response.setStatus("OK");
        response.setStatusCode(200);

        return  ResponseEntity.ok(response);
    }
}
