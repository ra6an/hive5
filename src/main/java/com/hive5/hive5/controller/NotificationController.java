package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PatchMapping("/{notificationId}/seen")
    public ResponseEntity<ApiResponse> seenNotification(@PathVariable Long notificationId, Principal principal) {
        Map<String, Object> data = notificationService.updateNotificationIsReadStatusToSeen(notificationId, principal);

        ApiResponse response = new ApiResponse();
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}
