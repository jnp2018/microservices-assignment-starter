package com.example.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults (level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    
    UserService userService;

    
    @GetMapping("user/exists/{userId}")
    public Boolean checkUserExist(@PathVariable("userId") long userId){
        return userService.checkUserExistById(userId);
    }
}
