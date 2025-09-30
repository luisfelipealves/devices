package com.example.devices.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeviceExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceDoesntExist {
    String message() default "Device not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}