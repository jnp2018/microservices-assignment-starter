package com.example.profile_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

    // quản lý việc chuyển tiếp token từ service này sang service khác
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // lấy các attributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                // lấy auth header từ attributes
                String authHeader = attributes.getRequest().getHeader("Authorization");
                if (authHeader != null) {
                    // set auth vào header
                    requestTemplate.header("Authorization", authHeader);
                }
            }
        };
    }
}