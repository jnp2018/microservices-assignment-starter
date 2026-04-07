package com.example.user_service.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.user_service.validation.validator.MatchPasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Target TYPE để validate theo DTO
@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Repeatable(MatchPasswordConstraint.List.class)
@Documented
@Constraint(validatedBy = {MatchPasswordValidator.class })
public @interface MatchPasswordConstraint {
    String message() default "CONFIRM_PASSWORD_NOT_MATCH";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MatchPasswordConstraint[] value();
    }
}
