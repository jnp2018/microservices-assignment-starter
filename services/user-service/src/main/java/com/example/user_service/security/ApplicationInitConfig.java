package com.example.user_service.security;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.enums.ErrorCode;
import com.example.user_service.exception.AppException;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            // Initialize roles
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(Role.builder()
                    .name("ADMIN")
                    .description("Administrator")
                    .build());
            }
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(Role.builder()
                    .name("USER")
                    .description("Regular user")
                    .build());
            }
            
            // Create admin1 user
            if (userRepository.findByUsername("admin1").isEmpty()) {
                var role = new HashSet<Role>();
                role.add(roleRepository.findByName("ADMIN").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
                role.add(roleRepository.findByName("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
                User user = User.builder()
                                .username("admin1")
                                .password(passwordEncoder.encode("admin1"))
                                .fullname("Admin User")
                                .email("admin1@example.com") 
                                .phoneNumber("0111111111111")
                                .roles(role)
                                .build();
                userRepository.save(user);
                System.out.println("Admin user created with username: admin1 and password: admin1");
            }
        };
    }
}
