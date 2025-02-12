package com.hive5.hive5.service;

import com.hive5.hive5.dto.FriendRequest.FriendRequestDTO;
import com.hive5.hive5.dto.Notification.NotificationDTO;
import com.hive5.hive5.dto.User.LoginRequest;
import com.hive5.hive5.dto.User.SignupRequest;
import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.FriendRequest;
import com.hive5.hive5.model.Notification;
import com.hive5.hive5.model.User;
import com.hive5.hive5.model.enums.FriendRequestStatus;
import com.hive5.hive5.repository.FriendRequestRepository;
import com.hive5.hive5.repository.NotificationRepository;
import com.hive5.hive5.repository.UserRepository;
import com.hive5.hive5.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;
    private final MessageService messageService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public Map<String, Object> register(SignupRequest signupRequest) {
        // Provjeriti da li username postoji u db
        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new CustomException("Username is already taken.", "Username is already taken.", 400);
        }

        // Provjeriti da li je password i confirmPassword isti
        if(!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new CustomException("Invalid request!", "Your password and confirm password does not match.", 400);
        }

        // Enkriptovanje passworda
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Kreiranje user-a
        User user = new User();
        user.setUsername(signupRequest.getUsername());
//        user.setEmail(signupRequest.getEmail());
        user.setPassword(encodedPassword);

        // Saveanje usera u db
        userRepository.save(user);

        UserDTO userData = new UserDTO(user);

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtUtil.generateToken(user.getUsername()));
        data.put("user", userData);

        return data;
    }

    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Authentifikacija usera
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Uzeti podatke usera iz baze
        Optional<User> user = userRepository.findByUsername(username);

        User userObj = user.orElseThrow(() -> new CustomException("Invalid credentials!", "You entered wrong Username or Password!", 400));

        UserDTO userDto = new UserDTO(userObj);

        Map<String, Object> initialData = getUserInitialState(userObj);

        String token = jwtUtil.generateToken(username);

        Map<String, Object> data = new HashMap<>();
        data.put("user", userDto);
        data.put("token", token);
        data.put("initialData", initialData);

        // Ako su credentials tacni generisemo i vracamo token
        return data;
    }

    public Map<String, Object> getUserDataFromToken(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Invalid Token!", "Token is not valid.", 400));

        UserDTO userDTO = new UserDTO(user);

        Map<String, Object> initialData = getUserInitialState(user);

        Map<String, Object> data = new HashMap<>();
        data.put("user", userDTO);
        data.put("initialData", initialData);

        return data;
    }

    private Map<String, Object> getUserInitialState(User user) {
        // Friend requests
        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
        List<FriendRequestDTO> friendRequestsDTO = friendRequests.stream().map(FriendRequestDTO::new).toList();

        // Friends
        List<FriendRequest> friends = friendRequestRepository.findAllFriendRequestByUser(user, FriendRequestStatus.ACCEPTED);
        List<FriendRequestDTO> friendsDTO = friends.stream().map(FriendRequestDTO::new).toList();

        // Notifications
        List<Notification> notifications = notificationRepository.findByReceiver(user);
        List<NotificationDTO> notificationsDTO = notifications.stream().map(NotificationDTO::new).toList();

        // Messages
        Map<String, Object> messages = messageService.getAllMessages(user);

        Map<String, Object> data = new HashMap<>();
        data.put("pendingFriendRequests", friendRequestsDTO);
        data.put("friends", friendsDTO);
        data.put("notifications", notificationsDTO);
        data.put("messages", messages.get("messages"));

        return data;
    }
}
