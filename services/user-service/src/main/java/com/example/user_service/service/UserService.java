package com.example.user_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_service.dto.request.RegisterRequest;
import com.example.user_service.dto.request.UserUpdateRequest;
import com.example.user_service.dto.response.UserResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.enums.ErrorCode;
import com.example.user_service.exception.AppException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
// import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    // đăng ký, check trùng thông tin trước khi đăng ký
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // PreAuth lọc yêu cầu trước khi vào hàm, không pass là cho không qua
    // PreAuthorize: chỉ user có permission READ_ALL_USERS mới được gọi
    @PreAuthorize("hasAuthority('PERMISSION_READ_ALL_USERS')")
    public List<UserResponse> getUsers(){
        log.info("Getting all users");
        log.info("Authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    // PostAuthorize: thực thi phương thức này rồi mới check quyền, nếu role sai thì chặn việc return
    // Dùng PostAuthorize ở đây vì cần lấy dữ liệu từ hãm findById. Nếu dùng PreAuthorize thì không lấy được user để so sánh với authentication.name
    // Cái return object ở đây là kiểu trả về của hàm, tức là UserResponse. Các trường của nó y hệt như các trường của UserResponse
    // authentication.name là username của người đang đăng nhập, với authentication là đối tượng đại diện cho người dùng đã đăng nhập, được Spring Security cung cấp 
    @PostAuthorize("hasRole('ADMIN') or returnObject.username == authentication.name")
    public UserResponse getUser(long userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
    
    public UserResponse updateUser(UserUpdateRequest request, long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    public Boolean checkUserExistById(long Id){
        return userRepository.existsById(Id);
    }

    // lấy thôn tin user hiện tại
    public UserResponse getCurrentUser(){
        var context = SecurityContextHolder.getContext();
        // lấy username từ contextholder (chính là claims subject đã gán khi tạo token )
        String username = context.getAuthentication().getName();
        // lấy thông tin user
        User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(currentUser);
    }

}
