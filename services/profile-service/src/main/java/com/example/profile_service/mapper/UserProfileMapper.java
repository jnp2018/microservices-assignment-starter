package com.example.profile_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.profile_service.dto.request.CreateProfileRequest;
import com.example.profile_service.dto.request.UpdateProfileRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(CreateProfileRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
    UserProfile updateUserProfile(@MappingTarget UserProfile userProfile, UpdateProfileRequest request);
}
