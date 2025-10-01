package com.example.devices.controller;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.service.DeviceService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@Tag(name = "Devices", description = "API for managing devices")
public class DevicesController {

    private static final Logger log = LoggerFactory.getLogger(DevicesController.class);
    private final DeviceService deviceService;

    public DevicesController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "Create a new device", description = "Creates a new device in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Device created successfully", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody CreateDeviceDTO createDeviceDTO) {
        log.info("Request to create device: {}", createDeviceDTO);
        DeviceDTO createdDevice = deviceService.createDevice(createDeviceDTO);
        log.info("Device created: {}", createdDevice);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing device", description = "Updates all fields of an existing device.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Device updated successfully", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Device not found", content = @Content)
    })
    @PutMapping
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody UpdateDeviceDTO updateDeviceDTO) {
        log.info("Request to update device: {}", updateDeviceDTO);
        DeviceDTO updatedDevice = deviceService.updateDevice(updateDeviceDTO);
        log.info("Device updated: {}", updatedDevice);
        return ResponseEntity.ok(updatedDevice);
    }

  @Operation(
      summary = "Partially update a device",
      description = "Applies a partial update to a device using JSON Patch (RFC 6902).")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Device patched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Malformed patch request",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Device not found", content = @Content)
      })

  @PatchMapping(path = "/{uuid}", consumes = "application/json-patch+json")
  public ResponseEntity<DeviceDTO> patchDevice(
      @Parameter(description = "The UUID of the device to patch")
          @PathVariable
          UUID uuid,
      @Parameter(description = "The JSON Patch to apply",
              example =
                      "{[{ \"op\": \"replace\", \"path\": \"/name\", \"value\": \"Patched Device Name\" }]}")
      @RequestBody JsonPatch patch) {
        log.info("Request to patch device with UUID: {}", uuid);
        DeviceDTO patchedDevice = deviceService.patchDevice(uuid, patch);
        log.info("Device patched: {}", patchedDevice);
        return ResponseEntity.ok(patchedDevice);
    }

    @Operation(summary = "Get a device by its UUID", description = "Retrieves a single device by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Device found", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Device not found", content = @Content)
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<DeviceDTO> getDeviceByUuid(@Parameter(description = "The UUID of the device") @PathVariable UUID uuid) {
        log.info("Request to get device by UUID: {}", uuid);
        DeviceDTO device = deviceService.getDeviceByUuid(uuid);
        log.info("Device found: {}", device);
        return ResponseEntity.ok(device);
    }

    @Operation(summary = "Get a paginated list of all devices", description = "Retrieves a paginated list of all devices. Supports sorting and pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<DeviceDTO>> getAllDevices(@Parameter(hidden = true) Pageable pageable) {
        log.info("Request to get all devices with pageable: {}", pageable);
        Page<DeviceDTO> devices = deviceService.getAllDevices(pageable);
        log.info("Found {} devices", devices.getTotalElements());
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Get a paginated list of devices by brand", description = "Retrieves a paginated list of devices filtered by a specific brand.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByBrand(
        @Parameter(description = "The brand to filter by") @PathVariable String brand, 
        @Parameter(hidden = true) Pageable pageable) {
        log.info("Request to get devices by brand: {} with pageable: {}", brand, pageable);
        Page<DeviceDTO> devices = deviceService.getDevicesByBrand(brand, pageable);
        log.info("Found {} devices for brand {}", devices.getTotalElements(), brand);
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Get a paginated list of devices by state", description = "Retrieves a paginated list of devices filtered by a specific state.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/state/{state}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByState(
        @Parameter(description = "The state to filter by (e.g., AVAILABLE, IN_USE)") @PathVariable String state, 
        @Parameter(hidden = true) Pageable pageable) {
        log.info("Request to get devices by state: {} with pageable: {}", state, pageable);
        Page<DeviceDTO> devices = deviceService.getDevicesByState(state, pageable);
        log.info("Found {} devices for state {}", devices.getTotalElements(), state);
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Delete a device by its UUID", description = "Deletes a device from the system. This operation is irreversible.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Device deleted successfully", content = @Content),
        @ApiResponse(responseCode = "400", description = "Validation error (e.g., device is in use)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Device not found", content = @Content)
    })
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@Parameter(description = "The UUID of the device to delete") @PathVariable String uuid) {
        log.info("Request to delete device with UUID: {}", uuid);
        deviceService.deleteDevice(uuid);
        log.info("Device with UUID {} deleted", uuid);
    }
}
