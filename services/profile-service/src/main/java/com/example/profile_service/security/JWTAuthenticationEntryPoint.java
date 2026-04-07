package com.example.profile_service.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.example.profile_service.dto.response.APIResponse;
import com.example.profile_service.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // bắt lỗi 401
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ERROR;
        
        // set http code
        response.setStatus(errorCode.getHttpStatusCode().value());
        // set loại body là json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // build response body lỗi
        APIResponse<?> apiResponse = APIResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build();
        
        // đổi body sang string json với object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String apiResponseJson = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(apiResponseJson);
        response.flushBuffer();
    }

}
