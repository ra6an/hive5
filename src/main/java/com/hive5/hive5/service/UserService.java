package com.hive5.hive5.service;

import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public Map<String,Object> findUserByUsername(@PathVariable String username, Principal principal) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Bad Request!", "No user with that username in DB.", 404));

        Map<String, Object> data = new HashMap<>();

        UserDTO userDTO = new UserDTO(user);
        data.put("user", userDTO);

        return data;
    }

    public Map<String, Object> findNewUsers(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "You are not authorized.", 404));

        List<FriendRequest> randomUsers = friendRequestRepository.findAllFriendRequestByUser(user, FriendRequestStatus.ACCEPTED);
        List<UUID> friendsId = new ArrayList<>(
                randomUsers.stream().map(fr -> fr.getSender().getId().equals(user.getId()) ?
                        fr.getReceiver().getId() :
                        fr.getSender().getId())
                .toList()
        );
        friendsId.add(user.getId());

        List<User> newUsers = userRepository.findRandomUsersExcludingFriends(friendsId);

        List<UserDTO> usersDTO = newUsers.stream().map(UserDTO::new).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("data", usersDTO);

        return data;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "User not found.", 401));
    }
}
