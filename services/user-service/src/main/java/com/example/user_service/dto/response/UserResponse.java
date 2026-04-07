package com.example.user_service.dto.response;

import java.util.Set;

import com.example.user_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor 
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String username;
    String fullname;
    String phoneNumber;
    String email;
    Set<Role> roles;
}
