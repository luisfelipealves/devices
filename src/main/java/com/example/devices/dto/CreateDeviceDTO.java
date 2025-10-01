package com.example.devices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data for creating a new device.")
public record CreateDeviceDTO(
    @NotBlank(message = "Name is required.")
    @Schema(description = "The name of the device.", requiredMode = Schema.RequiredMode.REQUIRED, example = "iPhone 15 Pro")
    String name,

    @NotBlank(message = "Brand is required.")
    @Schema(description = "The brand of the device.", requiredMode = Schema.RequiredMode.REQUIRED, example = "Apple")
    String brand,

    @Schema(description = "The initial state of the device. If not provided, defaults to 'AVAILABLE'.", example = "AVAILABLE")
    String state
) {}
