package com.example.devices.validator;
import com.example.devices.repository.DeviceRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DeviceExistsValidator implements ConstraintValidator<DeviceExists, String> {

    private final DeviceRepo deviceRepo;

    public DeviceExistsValidator(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    public boolean isValid(String deviceUuid, ConstraintValidatorContext context) {
        return deviceRepo.existsDeviceByUuid(UUID.fromString(deviceUuid));
    }
}