package com.example.profile_service.service;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.profile_service.dto.request.CreateProfileRequest;
import com.example.profile_service.dto.request.UpdateProfileRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.UserProfile;
import com.example.profile_service.enums.ErrorCode;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.httpclient.UserClient;
import com.example.profile_service.mapper.UserProfileMapper;
import com.example.profile_service.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    UserClient userClient;

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.userId == authentication.token.claims['userId'].toString()")
    public UserProfileResponse createProfile(CreateProfileRequest request) {
        try{
            Boolean isExists = userClient.checkUserExists(request.getUserId());
            if(isExists.equals(Boolean.FALSE)){
                throw new AppException(ErrorCode.USER_ID_NOT_FOUND);
            }
        } catch(Exception e){
            log.warn(e.getMessage());
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or userId == authentication.token.claims['userId'].toString()")
    public List<UserProfileResponse> getProfileByUserId(String userId) {

        try{
            Boolean isExists = userClient.checkUserExists(userId);
            if (isExists.equals(Boolean.FALSE)){
                throw new AppException(ErrorCode.USER_ID_NOT_FOUND);
            }
        } catch (Exception e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<UserProfile> userProfiles = userProfileRepository.findByUserId(userId);
        if (userProfiles == null || userProfiles.isEmpty()) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        if (userProfiles == null || userProfiles.isEmpty()) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }
    
    public UserProfileResponse updateProfile(String id, UpdateProfileRequest request) {
        UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public void deleteProfile(String id) {
        if (!userProfileRepository.existsById(id)) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        userProfileRepository.deleteById(id);
    }
}
