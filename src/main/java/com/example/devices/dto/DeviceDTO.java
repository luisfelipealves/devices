package com.example.devices.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a device in the system.")
public record DeviceDTO(
    @Schema(description = "The unique identifier of the device.", example = "123e4567-e89b-12d3-a456-426614174000")
    String uuid,

    @Schema(description = "The name of the device.", example = "iPhone 15 Pro")
    String name,

    @Schema(description = "The brand of the device.", example = "Apple")
    String brand,

    @Schema(description = "The current state of the device.", example = "AVAILABLE")
    String state,

    @Schema(description = "The timestamp when the device was created.", example = "2024-07-22T10:30:00")
    String creationTime
) {}
