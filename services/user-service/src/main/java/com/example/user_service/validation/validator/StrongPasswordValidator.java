package com.example.user_service.validation.validator;

import jakarta.validation.ConstraintValidator;
import java.util.regex.Pattern;

import com.example.user_service.validation.annotation.StrongPasswordConstraint;

public class StrongPasswordValidator implements ConstraintValidator<StrongPasswordConstraint, String> {
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*()\\-+].*");
    
    private int min;
    private int max;
    private boolean strongPassword;

    @Override
    public void initialize(StrongPasswordConstraint constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.strongPassword = constraintAnnotation.strongPassword();
    }

    @Override
    public boolean isValid(String password, jakarta.validation.ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }
        if (password.length() < min || password.length() > max) {
            return false;
        }
        if (strongPassword) {
            if (!UPPERCASE_PATTERN.matcher(password).matches()) {
                return false;
            }
            if (!LOWERCASE_PATTERN.matcher(password).matches()) {
                return false;
            }
            if (!DIGIT_PATTERN.matcher(password).matches()) {
                return false;
            }
            if (!SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
                return false;
            }
        }
        return true;
    }

}
