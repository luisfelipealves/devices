package com.example.devices.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.devices.repository.DeviceRepo;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class DeviceExistsValidatorTest {

    @Test
    void isValid_deviceExists_returnsTrue() {
        // Mock the repository to return an empty list, indicating the device is not in use
        DeviceRepo deviceRepository = mock(DeviceRepo.class);
        when(deviceRepository.existsDeviceByUuid(any())).thenReturn(true);

        // Create an instance of the validator with the mocked repository
        DeviceExistsValidator validator = new DeviceExistsValidator(deviceRepository);

        // Call isValid and assert that it returns true
        assertTrue(validator.isValid(UUID.randomUUID().toString(), null));
    }

    @Test
    void isValid_deviceDoesntExist_returnsFalse() {
        DeviceRepo deviceRepository = mock(DeviceRepo.class);
        when(deviceRepository.existsDeviceByUuid(any())).thenReturn(false);

        DeviceExistsValidator validator = new DeviceExistsValidator(deviceRepository);

        assertFalse(validator.isValid(UUID.randomUUID().toString(), null));
    }

}
