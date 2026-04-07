package com.example.profile_service.dto.response;

import com.example.profile_service.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class APIResponse<T> {
    @Builder.Default
    int code = ErrorCode.SUCCESS.getCode();

    @Builder.Default
    String message = ErrorCode.SUCCESS.getMessage();
    
    T data;
}
