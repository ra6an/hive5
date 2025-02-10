package com.hive5.hive5.controller;

import com.hive5.hive5.dto.ApiResponse;
import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final FriendRequestRepository friendRequestRepository;

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<ApiResponse> sendFriendRequest(@PathVariable UUID receiverId, Principal principal) {
        Map<String, Object> data = friendRequestService.sendFriendRequest(receiverId, principal);

        ApiResponse response = new ApiResponse();

        response.setMessage("Friend request successfully sent.");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{requestId}/accept")
    public ResponseEntity<ApiResponse> acceptFriendRequest(@PathVariable Long requestId, Principal principal) {
        Map<String, Object> data = friendRequestService.acceptFriendRequest(requestId, principal);

        ApiResponse response = new ApiResponse();
        response.setData(data);
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setMessage("Friend request accepted.");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("{requestId}/reject")
    public ResponseEntity<ApiResponse> rejectFriendRequest(@PathVariable Long requestId, Principal principal) {
        Map<String, Object> data = friendRequestService.rejectFriendRequest(requestId, principal);
        ApiResponse response = new ApiResponse();

        return ResponseEntity.ok(response);
    }

    @GetMapping("pending")
    public ResponseEntity<ApiResponse> getPendingRequests(Principal principal) {
        Map<String, Object> data = friendRequestService.getPendingRequests(principal);

        ApiResponse response = new ApiResponse();
        response.setData(data);
        response.setMessage("");
        response.setStatus("OK");
        response.setStatusCode(200);

        return ResponseEntity.ok(response);
    }
}
