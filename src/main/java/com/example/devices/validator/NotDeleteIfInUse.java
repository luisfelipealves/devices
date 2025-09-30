package com.example.devices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotDeleteIfInUseValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotDeleteIfInUse {
    String message() default "Device cannot be deleted if in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}