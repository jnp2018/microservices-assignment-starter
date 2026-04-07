package com.example.gateway.service;

import org.springframework.stereotype.Service;

import com.example.gateway.dto.request.IntrospectRequest;
import com.example.gateway.dto.response.APIResponse;
import com.example.gateway.dto.response.IntrospectResponse;
import com.example.gateway.httpclient.UserClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserClient userClient;

    // gọi đến user-service để introspect token
    public Mono<APIResponse<IntrospectResponse>> introspect(String token){
        return userClient.introspect(IntrospectRequest.builder().token(token).build());
    }
}