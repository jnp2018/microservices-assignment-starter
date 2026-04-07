package com.example.user_service.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.user_service.validation.validator.StrongPasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(StrongPasswordConstraint.List.class)
@Documented
@Constraint(validatedBy = {StrongPasswordValidator.class})
public @interface StrongPasswordConstraint {
    String message() default "PASSWORD_INVALID";

    int min() default 6;
    int max() default 30;
    boolean strongPassword() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        StrongPasswordConstraint[] value();
    }
}
