package com.hive5.hive5.service;

import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.FriendRequestStatus;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest sendFriendRequest(User sender, User receiver) {
        if(friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            throw new IllegalStateException("Friend request already exists.");
        }

        FriendRequest frinedRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();

        return friendRequestRepository.save(frinedRequest);
    }

    public FriendRequest acceptFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest rejectFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(FriendRequestStatus.DECLINED);
        return friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequest> getPendingRequests(User user) {
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
    }
}
