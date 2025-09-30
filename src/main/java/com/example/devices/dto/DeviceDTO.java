package com.example.devices.dto;

import com.example.devices.validator.NotUpdatableIfInUse;

@NotUpdatableIfInUse
public record DeviceDTO(String uuid, String name, String brand, String state, String creationTime) {}
