package com.example.devices.service;


import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.validator.DeviceExists;
import com.example.devices.validator.NotDeleteIfInUse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    DeviceDTO createDevice(DeviceDTO device);
    DeviceDTO updateDevice(@Valid UpdateDeviceDTO device);
    DeviceDTO getDeviceByUuid(UUID uuid);
    List<DeviceDTO> getAllDevices();
    List<DeviceDTO> getDevicesByBrand(String brand);
    List<DeviceDTO> getDevicesByState(String state);
    void deleteDevice(@Valid @DeviceExists @NotDeleteIfInUse String deviceUuid);
}
