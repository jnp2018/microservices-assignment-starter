package com.example.profile_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.profile_service.dto.request.CreateProfileRequest;
import com.example.profile_service.dto.response.APIResponse;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/create")
    public APIResponse<UserProfileResponse> createProfile(@RequestBody CreateProfileRequest request) {
        return APIResponse.<UserProfileResponse>builder().
        data(userProfileService.createProfile(request)).build();
    }
    
}
