package com.example.user_service.validation.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.user_service.validation.validator.DobValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Target field để validate theo trường dữ liệu
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "DOB_INVALID";

    int min();
    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}