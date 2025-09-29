package com.example.devices.service;

import com.example.devices.entity.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    Device createDevice(Device device);
    Device updateDevice(Device device);
    Device getDeviceByUuid(UUID uuid);
    List<Device> getAllDevices();
    List<Device> getDevicesByBrand(String brand);
    List<Device> getDevicesByState(String state);
    void deleteDevice(UUID id);
}
