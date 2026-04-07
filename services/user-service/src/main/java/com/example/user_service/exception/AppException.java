package com.example.user_service.exception;

import com.example.user_service.enums.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    
    ErrorCode errorCode;

}
