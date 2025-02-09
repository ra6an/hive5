package com.hive5.hive5.controller;

import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final FriendRequestRepository friendRequestRepository;

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<FriendRequest> sendFriendRequest(@PathVariable UUID receiverId, @RequestBody User sender) {
        User receiver = new User();
        receiver.setId(receiverId);

        FriendRequest request = friendRequestService.sendFriendRequest(sender, receiver);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<FriendRequest> acceptFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalRequest = friendRequestRepository.findById(requestId);

        if(optionalRequest.isEmpty()) {
            throw new IllegalStateException("There is no friend request to be accepted.");
        }

        FriendRequest request = optionalRequest.get();

        // TODO PROVJERITI DA LI JE RECEIVER ISTI KAO I USER KOJI PRIHVATA FRIEND REQUEST

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        return ResponseEntity.ok(request);
    }

    @PostMapping("{requestId}/reject")
    public ResponseEntity<FriendRequest> rejectFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalRequest = friendRequestRepository.findById(requestId);

        if(optionalRequest.isEmpty()) {
            throw new IllegalStateException("There is no friend request to be rejected.");
        }

        FriendRequest request = optionalRequest.get();

        // TODO PROVJERITI DA LI JE RECEIVER ISTI KAO I USER KOJI ODBIJA FRIEND REQUEST
        request.setStatus(FriendRequestStatus.DECLINED);
        friendRequestRepository.save(request);

        return ResponseEntity.ok(request);
    }

    @GetMapping("pending/{userId}")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(@PathVariable UUID userId) {
        User receiver = new User();
        receiver.setId(userId);
        return ResponseEntity.ok(friendRequestService.getPendingRequests(receiver));
    }
}
