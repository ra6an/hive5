package com.hive5.hive5.service;

import com.hive5.hive5.dto.User.UserDTO;
import com.hive5.hive5.exception.CustomException;
import com.hive5.hive5.model.User;
import com.hive5.hive5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Map<String,Object> findUserByUsername(@PathVariable String username, Principal principal) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Bad Request!", "No user with that username in DB.", 404));

        Map<String, Object> data = new HashMap<>();

        UserDTO userDTO = new UserDTO(user);
        data.put("user", userDTO);

        return data;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Unauthorized!", "User not found.", 401));
    }
}
