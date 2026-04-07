package com.example.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.AuthRequest;
import com.example.user_service.dto.request.IntrospectRequest;
import com.example.user_service.dto.request.LogoutRequest;
import com.example.user_service.dto.request.RegisterRequest;
import com.example.user_service.dto.request.UserUpdateRequest;
import com.example.user_service.dto.response.APIResponse;
import com.example.user_service.dto.response.AuthResponse;
import com.example.user_service.dto.response.IntrospectResponse;
import com.example.user_service.dto.response.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.service.AuthService;
import com.example.user_service.service.UserService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    
    @PostMapping("/register")
    public APIResponse<UserResponse> register (@RequestBody @Valid RegisterRequest request) {
        return APIResponse.<UserResponse>builder()
                .data(userService.register(request))
                .build();
    }
    
    @GetMapping
    public APIResponse<List<UserResponse>> getUsers() {
        var authen = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authenticated user: {}", authen.getName());
        authen.getAuthorities().forEach(grantedAuthor -> log.info(grantedAuthor.getAuthority()));
        APIResponse<List<UserResponse>> response = new APIResponse<>();
        response.setData(userService.getUsers());
        return response;
    }

    @GetMapping("/{userId}")
     public APIResponse<UserResponse> getUser(@PathVariable("userId") long userId) {
        APIResponse<UserResponse> response = new APIResponse<>();
        response.setData(userService.getUser(userId));
        return response;
    }

    @PutMapping("/{userId}")
    public APIResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable("userId") long userId) {
        APIResponse<UserResponse> response = new APIResponse<>();
        response.setData(userService.updateUser(request, userId));
        return response;
    }

    @DeleteMapping("/{userId}")
    APIResponse deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        APIResponse response = new APIResponse();
        return response;
    }
    
    @GetMapping("/myInfo")
    public APIResponse<UserResponse> getCurrentUser() {
       return APIResponse.<UserResponse>builder()
        .data(userService.getCurrentUser())
        .build();
    }
}
