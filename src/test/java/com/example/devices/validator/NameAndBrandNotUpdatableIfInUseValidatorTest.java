package com.example.devices.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class NameAndBrandNotUpdatableIfInUseValidatorTest {

  @Mock private DeviceRepo deviceRepo;

  @InjectMocks private NameAndBrandNotUpdatableIfInUseValidator validator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void isValid_ShouldReturnFalse_WhenInUseDeviceNameIsChanged() {
    UUID deviceUuid = UUID.randomUUID();
    Device existingDevice =
        Device.builder()
            .uuid(deviceUuid)
            .name("Old device")
            .brand("Old brand")
            .state(DeviceState.IN_USE)
            .build();

    UpdateDeviceDTO updatedDTO =
        new UpdateDeviceDTO(deviceUuid.toString(), "New device", "New brand", "AVAILABLE");

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(existingDevice));

    assertFalse(validator.isValid(updatedDTO, null));
  }

  @Test
  void isValid_ShouldReturnTrue_WhenNotInUseDeviceNameIsChanged() {
    UUID deviceUuid = UUID.randomUUID();
    Device existingDevice =
        Device.builder()
            .uuid(deviceUuid)
            .name("Old device")
            .brand("Old brand")
            .state(DeviceState.AVAILABLE)
            .build();

    UpdateDeviceDTO updatedDTO =
        new UpdateDeviceDTO(deviceUuid.toString(), "New device", "New brand", "AVAILABLE");

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(existingDevice));

    assertTrue(validator.isValid(updatedDTO, null));
  }
}
