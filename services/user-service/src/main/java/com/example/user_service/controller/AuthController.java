package com.example.user_service.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.AuthRequest;
import com.example.user_service.dto.request.IntrospectRequest;
import com.example.user_service.dto.request.LogoutRequest;
import com.example.user_service.dto.response.APIResponse;
import com.example.user_service.dto.response.AuthResponse;
import com.example.user_service.dto.response.IntrospectResponse;
import com.example.user_service.service.AuthService;
import com.nimbusds.jose.JOSEException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults (level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;
    
    @PostMapping("/login")
    public APIResponse<AuthResponse> auth(@RequestBody AuthRequest request) {
        APIResponse<AuthResponse> response = new APIResponse<>();
        var authResponse = authService.auth(request);
        response.setData(authResponse);
        return response;
    }

    @PostMapping("/logout")
    public APIResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authService.logout(request);
        return APIResponse.<Void>builder()
                .build();
    }

    @PostMapping("/introspect")
    public APIResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var introspectResponse = authService.introspect(request);
        return APIResponse.<IntrospectResponse>builder()
        .data(introspectResponse)
        .build();
    }
    
}
