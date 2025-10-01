package com.example.devices.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class NotDeleteIfInUseValidatorTest {

  @Test
  void isValid_deviceNotInUse_returnsTrue() {
    // Mock the repository to return an empty list, indicating the device is not in use
    DeviceRepo deviceRepository = mock(DeviceRepo.class);
    when(deviceRepository.findDeviceByUuid(any())).thenReturn(Optional.empty());

    // Create an instance of the validator with the mocked repository
    NotDeleteIfInUseValidator validator = new NotDeleteIfInUseValidator(deviceRepository);

    // Call isValid and assert that it returns true
    assertTrue(validator.isValid(UUID.randomUUID().toString(), null));
  }

  @Test
  void isValid_deviceInUse_returnsFalse() {
    Device existingDevice =
        Device.builder()
            .uuid(UUID.randomUUID())
            .name("Old device")
            .brand("Old brand")
            .state(DeviceState.IN_USE)
            .build();
    DeviceRepo deviceRepository = mock(DeviceRepo.class);
    when(deviceRepository.findDeviceByUuid(any())).thenReturn(Optional.of(existingDevice));

    NotDeleteIfInUseValidator validator = new NotDeleteIfInUseValidator(deviceRepository);

    assertFalse(validator.isValid(UUID.randomUUID().toString(), null));
  }
}
