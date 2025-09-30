package com.example.devices.service.impl;

import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepo;
import com.example.devices.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

  private final DeviceRepo deviceRepo;
  private final DeviceMapper deviceMapper;

  @Autowired
  public DeviceServiceImpl(DeviceRepo deviceRepo, DeviceMapper deviceMapper) {
    this.deviceRepo = deviceRepo;
    this.deviceMapper = deviceMapper;
  }

  @Override
  @Transactional
  public DeviceDTO createDevice(DeviceDTO deviceDTO) {
    Device device = deviceMapper.toEntity(deviceDTO);
    device.setUuid(UUID.randomUUID());
    device.setCreationTime(LocalDateTime.now());
    if (device.getState() == null) {
      device.setState(DeviceState.AVAILABLE);
    }
    return deviceMapper.toDto(deviceRepo.save(device));
  }

  @Override
  @Transactional
  public DeviceDTO updateDevice(UpdateDeviceDTO deviceDTO) {
    Device existingDevice =
        deviceRepo
            .findDeviceByUuid(UUID.fromString(deviceDTO.uuid()))
            .orElseThrow(() -> new EntityNotFoundException("Device not found with UUID: " + deviceDTO.uuid()));
    existingDevice.setName(deviceDTO.name());
    existingDevice.setBrand(deviceDTO.brand());
    existingDevice.setState(DeviceState.valueOf(deviceDTO.state()));

    return deviceMapper.toDto(deviceRepo.save(existingDevice));
  }

  @Override
  public DeviceDTO getDeviceByUuid(UUID uuid) {
    return deviceMapper.toDto(
        deviceRepo
            .findDeviceByUuid(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Device not found with UUID: " + uuid)));
  }

  @Override
  public List<DeviceDTO> getAllDevices() {
    return deviceMapper.toDtoList(deviceRepo.findAll());
  }

  @Override
  public List<DeviceDTO> getDevicesByBrand(String brand) {
    return deviceMapper.toDtoList(deviceRepo.findByBrand(brand));
  }

  @Override
  public List<DeviceDTO> getDevicesByState(String state) {
    DeviceState deviceState = DeviceState.valueOf(state.toUpperCase());
    return deviceMapper.toDtoList(deviceRepo.findByState(deviceState));
  }

  @Override
  @Transactional
  public void deleteDevice(String deviceUuid) {
    deviceRepo.deleteDeviceByUuid(UUID.fromString(deviceUuid));
  }
}
