package com.example.profile_service.dto.request;

import jakarta.validation.constraints.NotBlank;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    @NotBlank(message = "BLANK_FIRSTNAME")
    String firstname;

    @NotBlank(message = "BLANK_LASTNAME")
    String lastname;

    @NotBlank(message = "BLANK_ADDRESS")
    String address;
}
