package com.example.profile_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.profile_service.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    List<UserProfile> findByUserId(String userId);
    Optional<UserProfile> findById(String id);
}
