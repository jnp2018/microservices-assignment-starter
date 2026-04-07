package com.example.profile_service.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    SUCCESS(200, "Thành công", HttpStatus.OK),
    UNAUTHORIZED_ERROR(401, "Yêu cầu cần được xác thực", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(101, "Mh lỗi không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1001, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXISTS(1002, "Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1003, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1004, "Số điện thoại đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1005, "Tên đăng nhập phải từ 3 đến 50 ký tự", HttpStatus.BAD_REQUEST), 
    UNAUTHENTICATED(1006, "Chưa xác thực danh tính", HttpStatus.UNAUTHORIZED),
    PASSWORD_INVALID(1007, "Mật khẩu phải từ 6 đến 30 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1008, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(1009, "Không tìm thấy vai trò/quyền hạn", HttpStatus.NOT_FOUND), 
    INVALID_TOKEN(1010, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1011, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1012, "Tên đăng nhập hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED),
    
    USERNAME_NOT_BLANK(1013, "Tên đăng nhập không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_BLANK(1014, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_NOT_BLANK(1015, "Mật khẩu xác nhận không được để trống", HttpStatus.BAD_REQUEST),
    FULLNAME_NOT_BLANK(1016, "Họ và tên không được để trống", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_BLANK(1017, "Email không được để trống", HttpStatus.BAD_REQUEST),
    PHONE_NOT_BLANK(1019, "Số điện thoại không được để trống", HttpStatus.BAD_REQUEST),
    DOB_NOT_NULL(1021, "Ngày sinh không được để trống", HttpStatus.BAD_REQUEST),
    
    EMAIL_INVALID(1018, "Định dạng email không hợp lệ", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1020, "Số điện thoại phải bao gồm đúng 10 chữ số", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1022, "Độ tuổi hợp lệ là từ 12 đến 100 tuổi", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_NOT_MATCH(1023, "Mật khẩu xác nhận không khớp với mật khẩu gốc", HttpStatus.BAD_REQUEST),
    // INVALID_KEY(1001, "Mh lỗi không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_ID_NOT_FOUND(1002, "Không tìm thấy userId", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(1003, "Lỗi máy chủ", HttpStatus.INTERNAL_SERVER_ERROR),
    PROFILE_NOT_FOUND(1004, "Không tìm thấy profile", HttpStatus.NOT_FOUND),
    ;
    

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}