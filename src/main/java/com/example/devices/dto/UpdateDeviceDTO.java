package com.example.devices.dto;

import com.example.devices.validator.DeviceExists;
import com.example.devices.validator.NameAndBrandNotUpdatableIfInUse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@NameAndBrandNotUpdatableIfInUse
public record UpdateDeviceDTO(
    @NotNull(message = "UUID is required.") @DeviceExists(message = "Device not found") String uuid,
    @NotBlank(message = "Name is required.") String name,
    @NotBlank(message = "Brand is required.") String brand,
    String state) {}
