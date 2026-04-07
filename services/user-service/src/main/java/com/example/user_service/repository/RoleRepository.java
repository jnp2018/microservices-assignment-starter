package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_service.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
    Optional<Role> findByName(String name);
}
