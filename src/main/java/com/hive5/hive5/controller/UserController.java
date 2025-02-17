package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.dto.User.LoginRequest;
import com.hive5.hive5.service.AuthService;
import com.hive5.hive5.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUserDataFromToken(Principal principal) {
        Map<String, Object> data = authService.getUserDataFromToken(principal);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setMessage("");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String username, Principal principal) {
        Map<String, Object> data = userService.findUserByUsername(username, principal);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setMessage("");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody LoginRequest loginRequest, Principal principal) {
        Map<String, Object> data = authService.updateUser(loginRequest, principal);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setMessage("");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-new-friends")
    public ResponseEntity<ApiResponse> findNewFriends(Principal principal) {
        Map<String,Object> data = userService.findNewUsers(principal);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setMessage("");
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}
