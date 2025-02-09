package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.dto.User.LoginRequest;
import com.hive5.hive5.dto.User.SignupRequest;
import com.hive5.hive5.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest signupRequest) {
        Map<String, Object> data = authService.register(signupRequest);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setMessage("User successfully registered.");
        response.setStatusCode(200);
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> data = authService.login(loginRequest);

        ApiResponse response = new ApiResponse();
        response.setStatus("OK");
        response.setMessage("You are logged in.");
        response.setStatusCode(200);
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}
