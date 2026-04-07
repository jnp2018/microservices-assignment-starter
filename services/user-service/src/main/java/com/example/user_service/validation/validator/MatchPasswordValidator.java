package com.example.user_service.validation.validator;

import com.example.user_service.dto.request.RegisterRequest;
import com.example.user_service.validation.annotation.MatchPasswordConstraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchPasswordValidator implements ConstraintValidator<MatchPasswordConstraint, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return true; 
        }

        boolean isValid = request.getPassword() != null && request.getPassword().equals(request.getConfirmPassword());
        return isValid;
    }

}
