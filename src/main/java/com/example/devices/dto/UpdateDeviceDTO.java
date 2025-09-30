package com.example.devices.dto;

import com.example.devices.validator.DeviceExists;
import com.example.devices.validator.NameAndBrandNotUpdatableIfInUse;
import jakarta.validation.constraints.NotNull;

@NameAndBrandNotUpdatableIfInUse
public record UpdateDeviceDTO(
    @NotNull @DeviceExists String uuid,
    String name,
    String brand,
    String state,
    String creationTime) {}
