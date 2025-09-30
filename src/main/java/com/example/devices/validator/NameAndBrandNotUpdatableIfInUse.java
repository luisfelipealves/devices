package com.example.devices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameAndBrandNotUpdatableIfInUseValidator.class)
@Documented
public @interface NameAndBrandNotUpdatableIfInUse {
    String message() default "Name and brand properties cannot be updated if the device is in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
