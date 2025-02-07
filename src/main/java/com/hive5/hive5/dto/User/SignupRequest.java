package com.hive5.hive5.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String username;
//    private String email;
    private String password;
    private String confirmPassword;
}
