package com.hive5.hive5.service;

import com.hive5.hive5.dto.FriendRequest.FriendRequestDTO;
import com.hive5.hive5.dto.Notification.CreateNotificationRequest;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.NotificationType;
import com.hive5.hive5.model.enums.TargetType;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final WSNotificationService wsNotificationService;

    public Map<String, Object> sendFriendRequest(@PathVariable UUID receiverId, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 401));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException("Bad Request!", "There is no user in DB with that ID.", 404));

        if(!friendRequestRepository.findByUsersAndStatus(user, receiver, FriendRequestStatus.DECLINED).isEmpty()) {
            throw new CustomException("Bad Request", "Friend request already exists.", 404);
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(user)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);

        FriendRequestDTO frDTO = new FriendRequestDTO(friendRequest);

        Map<String, Object> data = new HashMap<>();

        data.put("request", frDTO);

        wsNotificationService.sendDataViaWS(receiverId, data, WSType.FRIENDREQUEST);

        return data;
    }

    public Map<String, Object> acceptFriendRequest(@PathVariable long friendRequestId, Principal principal) {
        String username = principal.getName();

        return checkIfIsValidRequest(username, friendRequestId, FriendRequestStatus.ACCEPTED);
    }

    public Map<String, Object> rejectFriendRequest(long friendRequestId, Principal principal) {
        String username = principal.getName();

        return checkIfIsValidRequest(username, friendRequestId, FriendRequestStatus.DECLINED);
    }

    public Map<String, Object> getPendingRequests(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized", 401));

        List<FriendRequest> sender = friendRequestRepository.findBySenderAndStatus(user, FriendRequestStatus.PENDING);
        List<FriendRequest> receiver = friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);

        List<FriendRequestDTO> senderDTO = sender.stream().map(FriendRequestDTO::new).toList();
        List<FriendRequestDTO> receiverDTO = receiver.stream().map(FriendRequestDTO::new).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("sentRequests", senderDTO);
        data.put("receivedRequests", receiverDTO);

        return data;
    }

    private Map<String, Object> checkIfIsValidRequest(String username, long friendRequestId, FriendRequestStatus frs) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized", 401));

        FriendRequest fr = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new CustomException("Bad Request!", "No friend request in DB.", 404));

        if(!fr.getReceiver().getId().equals(user.getId()))
            throw new CustomException("Unauthorized!", "You are not authorized", 401);

        if(!fr.getStatus().equals(FriendRequestStatus.PENDING))
            throw new CustomException("Bad Request!", "No friend request in DB.", 404);

        fr.setStatus(frs);

        friendRequestRepository.save(fr);

        FriendRequestDTO frDTO = new FriendRequestDTO(fr);

        Map<String, Object> data = new HashMap<>();
        data.put("data", frDTO);

        // Kreiramo notifikaciju ako je friend request acceptan
        if(frs.equals(FriendRequestStatus.ACCEPTED)) {
            CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
            createNotificationRequest.setReceiver(fr.getSender());
            createNotificationRequest.setSender(user);
            createNotificationRequest.setType(NotificationType.FRIEND_REQUEST_ACCEPTED);
            createNotificationRequest.setTargetType(TargetType.FRIEND_REQUEST);
            createNotificationRequest.setTargetId(fr.getId());
            createNotificationRequest.setSecondaryTargetId(null);

            Map<String, Object> notificationData = notificationService.createNotification(createNotificationRequest);
            notificationData.put("friendRequestData", frDTO);

            // TODO :: nakon implementacije socketa poslati useru notifikaciju.
            wsNotificationService.sendDataViaWS(createNotificationRequest.getReceiver().getId(), notificationData, WSType.NOTIFICATION);
        }

        return data;
    }
}
