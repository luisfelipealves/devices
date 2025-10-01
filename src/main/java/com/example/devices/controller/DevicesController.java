package com.example.devices.controller;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
public class DevicesController {

    private final DeviceService deviceService;

    public DevicesController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody CreateDeviceDTO createDeviceDTO) {
        DeviceDTO createdDevice = deviceService.createDevice(createDeviceDTO);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody UpdateDeviceDTO updateDeviceDTO) {
        DeviceDTO updatedDevice = deviceService.updateDevice(updateDeviceDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<DeviceDTO> getDeviceByUuid(@PathVariable UUID uuid) {
        DeviceDTO device = deviceService.getDeviceByUuid(uuid);
        return ResponseEntity.ok(device);
    }

    @GetMapping
    public ResponseEntity<Page<DeviceDTO>> getAllDevices(Pageable pageable) {
        Page<DeviceDTO> devices = deviceService.getAllDevices(pageable);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByBrand(@PathVariable String brand, Pageable pageable) {
        Page<DeviceDTO> devices = deviceService.getDevicesByBrand(brand, pageable);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<Page<DeviceDTO>> getDevicesByState(@PathVariable String state, Pageable pageable) {
        Page<DeviceDTO> devices = deviceService.getDevicesByState(state, pageable);
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable String uuid) {
        deviceService.deleteDevice(uuid);
    }
}
