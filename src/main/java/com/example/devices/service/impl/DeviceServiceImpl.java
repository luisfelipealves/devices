package com.example.devices.service.impl;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import com.example.devices.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceServiceImpl implements DeviceService {

  private final DeviceRepo deviceRepo;

  @Autowired
  public DeviceServiceImpl(DeviceRepo deviceRepo) {
    this.deviceRepo = deviceRepo;
  }

  @Override
  @Transactional
  public Device createDevice(Device device) {
    device.setUuid(UUID.randomUUID());
    device.setCreationTime(LocalDateTime.now());
    if (device.getState() == null) {
      device.setState(DeviceState.AVAILABLE);
    }
    return deviceRepo.save(device);
  }

  @Override
  @Transactional
  public Device updateDevice(Device device) {
    Device existingDevice =
        deviceRepo
            .findDeviceByUuid(device.getUuid())
            .orElseThrow(
                () ->
                    new EntityNotFoundException("Device not found with UUID: " + device.getUuid()));

    existingDevice.setName(device.getName());
    existingDevice.setBrand(device.getBrand());
    existingDevice.setState(device.getState());

    return deviceRepo.save(existingDevice);
  }

  @Override
  public Device getDeviceByUuid(UUID uuid) {
    return deviceRepo
        .findDeviceByUuid(uuid)
        .orElseThrow(() -> new EntityNotFoundException("Device not found with UUID: " + uuid));
  }

  @Override
  public List<Device> getAllDevices() {
    return deviceRepo.findAll();
  }

  @Override
  public List<Device> getDevicesByBrand(String brand) {
    return deviceRepo.findByBrand(brand);
  }

  @Override
  public List<Device> getDevicesByState(String state) {
    DeviceState deviceState = DeviceState.valueOf(state.toUpperCase());
    return deviceRepo.findByState(deviceState);
  }

  @Override
  @Transactional
  public void deleteDevice(UUID uuid) {
    deviceRepo.deleteDeviceByUuid(uuid);
  }
}
