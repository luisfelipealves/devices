package com.example.devices.validator;

import com.example.devices.dto.DeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

public class InUseDeviceValidator implements ConstraintValidator<NotUpdatableIfInUse, DeviceDTO> {

  private final DeviceRepo deviceRepo;

  @Autowired
  public InUseDeviceValidator(DeviceRepo deviceRepo) {
    this.deviceRepo = deviceRepo;
  }

  @Override
  public boolean isValid(DeviceDTO deviceDTO, ConstraintValidatorContext context) {
    Optional<Device> existingDeviceOptional =
        deviceRepo.findDeviceByUuid(UUID.fromString(deviceDTO.uuid()));
    if (existingDeviceOptional.isPresent()) {
      Device existingDevice = existingDeviceOptional.get();
      return existingDevice.getState() != DeviceState.IN_USE
          || (existingDevice.getName().equals(deviceDTO.name())
              && existingDevice.getBrand().equals(deviceDTO.brand()));
    }
    return true;
  }
}
