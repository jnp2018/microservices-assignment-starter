package com.example.user_service.dto.request;

import java.time.LocalDate;

import com.example.user_service.validation.annotation.DobConstraint;
import com.example.user_service.validation.annotation.MatchPasswordConstraint;
import com.example.user_service.validation.annotation.StrongPasswordConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MatchPasswordConstraint()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "USERNAME_NOT_BLANK")
    @Size(min = 3, max = 50, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @StrongPasswordConstraint
    String password;

    @NotBlank(message = "CONFIRM_PASSWORD_NOT_BLANK")
    String confirmPassword;

    @NotBlank(message = "FULLNAME_NOT_BLANK")
    String fullname;

    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_NOT_BLANK")
    @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_INVALID")
    String phoneNumber;

    @DobConstraint(min = 12, max = 100, message = "DOB_INVALID")
    LocalDate dob;
}
