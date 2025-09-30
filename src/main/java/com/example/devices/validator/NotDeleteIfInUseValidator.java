package com.example.devices.validator;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NotDeleteIfInUseValidator implements ConstraintValidator<NotDeleteIfInUse, String> {

    private final DeviceRepo deviceRepo;

    public NotDeleteIfInUseValidator(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    @Override
    public boolean isValid(String deviceUuid, ConstraintValidatorContext context) {
        Optional<Device> existingDeviceOptional = deviceRepo.findDeviceByUuid(UUID.fromString(deviceUuid));
        if (existingDeviceOptional.isPresent()) {
            Device existingDevice = existingDeviceOptional.get();
            return existingDevice.getState() != DeviceState.IN_USE;
        }
        return true;
    }
}