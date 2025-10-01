package com.example.devices.service;


import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.validator.DeviceExists;
import com.example.devices.validator.NotDeleteIfInUse;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {
    DeviceDTO createDevice(CreateDeviceDTO device);
    DeviceDTO updateDevice(@Valid UpdateDeviceDTO device);
    DeviceDTO patchDevice(UUID uuid, JsonPatch patch);
    DeviceDTO getDeviceByUuid(UUID uuid);
    Page<DeviceDTO> getAllDevices(Pageable pageable);
    Page<DeviceDTO> getDevicesByBrand(String brand, Pageable pageable);
    Page<DeviceDTO> getDevicesByState(String state, Pageable pageable);
    void deleteDevice(@Valid @DeviceExists @NotDeleteIfInUse String deviceUuid);
}
