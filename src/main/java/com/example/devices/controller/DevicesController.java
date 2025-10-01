package com.example.devices.controller;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.service.DeviceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
public class DevicesController {

    private static final Logger log = LoggerFactory.getLogger(DevicesController.class);
    private final DeviceService deviceService;

    public DevicesController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody CreateDeviceDTO createDeviceDTO) {
        log.info("Request to create device: {}", createDeviceDTO);
        DeviceDTO createdDevice = deviceService.createDevice(createDeviceDTO);
        log.info("Device created: {}", createdDevice);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody UpdateDeviceDTO updateDeviceDTO) {
        log.info("Request to update device: {}", updateDeviceDTO);
        DeviceDTO updatedDevice = deviceService.updateDevice(updateDeviceDTO);
        log.info("Device updated: {}", updatedDevice);
        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<DeviceDTO> getDeviceByUuid(@PathVariable UUID uuid) {
        log.info("Request to get device by UUID: {}", uuid);
        DeviceDTO device = deviceService.getDeviceByUuid(uuid);
        log.info("Device found: {}", device);
        return ResponseEntity.ok(device);
    }

    @GetMapping
    public ResponseEntity<Page<DeviceDTO>> getAllDevices(Pageable pageable) {
        log.info("Request to get all devices with pageable: {}", pageable);
        Page<DeviceDTO> devices = deviceService.getAllDevices(pageable);
        log.info("Found {} devices", devices.getTotalElements());
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByBrand(@PathVariable String brand, Pageable pageable) {
        log.info("Request to get devices by brand: {} with pageable: {}", brand, pageable);
        Page<DeviceDTO> devices = deviceService.getDevicesByBrand(brand, pageable);
        log.info("Found {} devices for brand {}", devices.getTotalElements(), brand);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByState(@PathVariable String state, Pageable pageable) {
        log.info("Request to get devices by state: {} with pageable: {}", state, pageable);
        Page<DeviceDTO> devices = deviceService.getDevicesByState(state, pageable);
        log.info("Found {} devices for state {}", devices.getTotalElements(), state);
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable String uuid) {
        log.info("Request to delete device with UUID: {}", uuid);
        deviceService.deleteDevice(uuid);
        log.info("Device with UUID {} deleted", uuid);
    }
}
