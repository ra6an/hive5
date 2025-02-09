package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

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
}
