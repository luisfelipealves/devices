package com.example.devices.service.impl;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepo;
import com.example.devices.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

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
  public DeviceDTO createDevice(CreateDeviceDTO deviceDTO) {
    Device device = deviceMapper.toEntity(deviceDTO);
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
  public Page<DeviceDTO> getAllDevices(Pageable pageable) {
    Page<Device> devicePage = deviceRepo.findAll(pageable);
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  public Page<DeviceDTO> getDevicesByBrand(String brand, Pageable pageable) {
    Page<Device> devicePage = deviceRepo.findByBrand(brand, pageable);
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  public Page<DeviceDTO> getDevicesByState(String state, Pageable pageable) {
    DeviceState deviceState = DeviceState.valueOf(state.toUpperCase());
    Page<Device> devicePage = deviceRepo.findByState(deviceState, pageable);
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  @Transactional
  public void deleteDevice(String deviceUuid) {
    deviceRepo.deleteDeviceByUuid(UUID.fromString(deviceUuid));
  }
}
