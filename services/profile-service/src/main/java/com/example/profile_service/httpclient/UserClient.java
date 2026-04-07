package com.example.profile_service.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping(value = "/internal/user/exists/{userId}", consumes = "application/json")
    Boolean checkUserExists(@PathVariable("userId") String userId);
}
