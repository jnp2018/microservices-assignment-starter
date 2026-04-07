package com.example.user_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.user_service.dto.response.APIResponse;
import com.example.user_service.enums.ErrorCode;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Lỗi Runtime thì trả về 400 Bad request
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        APIResponse response = APIResponse.builder()
                .code(400)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    // Exception chung (các lỗi còn lại) thì trả về 500 Internal Server Error
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleException(Exception ex) {
        APIResponse response = APIResponse.builder()
                .code(500)
                .message("Lỗi máy chủ")
                .build();
        return ResponseEntity.internalServerError().body(response);
    }

    // Lỗi AppException thì trả về mã lỗi trong ErrorCode và thông báo tương ứng
    @ExceptionHandler(AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse response = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(response);
    }

    // Lỗi ValidationException thì trả về 400 Bad Request và thông báo lỗi chi tiết
    @ExceptionHandler(ValidationException.class)
    ResponseEntity<APIResponse> handleValidationException(ValidationException ex) {
        APIResponse response = APIResponse.builder()
                .code(400)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    // Lỗi IllegalArgumentException thì trả về 400 Bad Request và thông báo lỗi chi tiết
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        APIResponse response = APIResponse.builder()
                .code(400)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    // Lỗi NullPointerException thì trả về 400 Bad Request và thông báo lỗi chi tiết
    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<APIResponse> handleNullPointerException(NullPointerException ex) {
        APIResponse response = APIResponse.builder()
                .code(400)
                .message("Dữ liệu không được để trống")
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    // Lỗi MethodArgumentNotValidException thì trả về mã lỗi và thông báo tương ứng trong ErrorCode
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String errorKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(errorKey);

        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.INVALID_KEY;
        }
        APIResponse response = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status((errorCode.getHttpStatusCode())).body(response);
    }

    // Lỗi AccessDeniedException thì trả về 403 Forbidden và thông báo tương ứng trong ErrorCode
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                            .body(APIResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build());
    }
}
