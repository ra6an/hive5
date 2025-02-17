package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.dto.Post.CreatePostRequest;
import com.hive5.hive5.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/explore")
    public ResponseEntity<ApiResponse> getPosts(Principal principal) {
        Map<String, Object> postsData = postService.getPosts("explore", principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(postsData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse> getPostsHome(Principal principal) {
        Map<String, Object> postsData = postService.getPosts("home", principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(postsData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse> getPostsFromUser(@PathVariable String username) {
        Map<String, Object> postData = postService.getPostsFromUser(username);

        ApiResponse response = new ApiResponse();
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(postData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable long postId, Principal principal) {
        Map<String, Object> postData = postService.getPost(postId, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(postData);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPost(@RequestBody CreatePostRequest createPostRequest, Principal principal) {
        Map<String, Object> postData = postService.createPost(createPostRequest, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("Your post has been created.");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(postData);

        return ResponseEntity.ok(response);
    }
}
