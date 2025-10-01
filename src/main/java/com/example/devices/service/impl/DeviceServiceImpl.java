package com.example.devices.service.impl;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepo;
import com.example.devices.service.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);
  private final DeviceRepo deviceRepo;
  private final DeviceMapper deviceMapper;
  private final ObjectMapper objectMapper;

  @Autowired
  public DeviceServiceImpl(DeviceRepo deviceRepo, DeviceMapper deviceMapper, ObjectMapper objectMapper) {
    this.deviceRepo = deviceRepo;
    this.deviceMapper = deviceMapper;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public DeviceDTO createDevice(CreateDeviceDTO deviceDTO) {
    log.debug("Creating device with data: {}", deviceDTO);
    Device device = deviceMapper.toEntity(deviceDTO);
    if (device.getState() == null) {
      device.setState(DeviceState.AVAILABLE);
      log.debug("Device state not provided, defaulting to AVAILABLE");
    }
    Device savedDevice = deviceRepo.save(device);
    log.info("Device created successfully with UUID: {}", savedDevice.getUuid());
    return deviceMapper.toDto(savedDevice);
  }

  @Override
  @Transactional
  public DeviceDTO updateDevice(UpdateDeviceDTO deviceDTO) {
    log.debug("Updating device with UUID: {}", deviceDTO.uuid());
    UUID deviceUuid = UUID.fromString(deviceDTO.uuid());
    Device existingDevice =
        deviceRepo
            .findDeviceByUuid(deviceUuid)
            .orElseThrow(() -> {
              log.error("Device not found with UUID: {}", deviceUuid);
              return new EntityNotFoundException("Device not found with UUID: " + deviceUuid);
            });
    existingDevice.setName(deviceDTO.name());
    existingDevice.setBrand(deviceDTO.brand());
    existingDevice.setState(DeviceState.valueOf(deviceDTO.state()));

    Device updatedDevice = deviceRepo.save(existingDevice);
    log.info("Device with UUID {} updated successfully.", updatedDevice.getUuid());
    return deviceMapper.toDto(updatedDevice);
  }

  @Override
  @Transactional
  public DeviceDTO patchDevice(UUID uuid, JsonPatch patch) {
    log.debug("Patching device with UUID: {}", uuid);
    Device existingDevice = deviceRepo.findDeviceByUuid(uuid).orElseThrow(() -> {
        log.error("Device not found with UUID: {}", uuid);
        return new EntityNotFoundException("Device not found with UUID: " + uuid);
    });

    try {
        JsonNode patched = patch.apply(objectMapper.convertValue(existingDevice, JsonNode.class));
        Device patchedDevice = objectMapper.treeToValue(patched, Device.class);
        // Restore non-updatable fields
        patchedDevice.setId(existingDevice.getId());
        patchedDevice.setUuid(existingDevice.getUuid());
        patchedDevice.setCreationTime(existingDevice.getCreationTime());

        Device savedDevice = deviceRepo.save(patchedDevice);
        log.info("Device with UUID {} patched successfully.", savedDevice.getUuid());
        return deviceMapper.toDto(savedDevice);
    } catch (JsonPatchException | JsonProcessingException e) {
        log.error("Error applying patch to device with UUID: {}", uuid, e);
        throw new RuntimeException("Error applying patch", e);
    }
  }

  @Override
  public DeviceDTO getDeviceByUuid(UUID uuid) {
    log.debug("Fetching device with UUID: {}", uuid);
    Device device =
        deviceRepo
            .findDeviceByUuid(uuid)
            .orElseThrow(() -> {
              log.error("Device not found with UUID: {}", uuid);
              return new EntityNotFoundException("Device not found with UUID: " + uuid);
            });
    log.info("Device found with UUID: {}", uuid);
    return deviceMapper.toDto(device);
  }

  @Override
  public Page<DeviceDTO> getAllDevices(Pageable pageable) {
    log.debug("Fetching all devices for page request: {}", pageable);
    Page<Device> devicePage = deviceRepo.findAll(pageable);
    log.info("Found {} devices on page {} of {}", devicePage.getNumberOfElements(), devicePage.getNumber(), devicePage.getTotalPages());
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  public Page<DeviceDTO> getDevicesByBrand(String brand, Pageable pageable) {
    log.debug("Fetching devices by brand '{}' for page request: {}", brand, pageable);
    Page<Device> devicePage = deviceRepo.findByBrand(brand, pageable);
    log.info("Found {} devices for brand '{}' on page {} of {}", devicePage.getNumberOfElements(), brand, devicePage.getNumber(), devicePage.getTotalPages());
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  public Page<DeviceDTO> getDevicesByState(String state, Pageable pageable) {
    log.debug("Fetching devices by state '{}' for page request: {}", state, pageable);
    DeviceState deviceState = DeviceState.valueOf(state.toUpperCase());
    Page<Device> devicePage = deviceRepo.findByState(deviceState, pageable);
    log.info("Found {} devices for state '{}' on page {} of {}", devicePage.getNumberOfElements(), state, devicePage.getNumber(), devicePage.getTotalPages());
    return devicePage.map(deviceMapper::toDto);
  }

  @Override
  @Transactional
  public void deleteDevice(String deviceUuid) {
    log.debug("Deleting device with UUID: {}", deviceUuid);
    deviceRepo.deleteDeviceByUuid(UUID.fromString(deviceUuid));
    log.info("Device with UUID {} deleted successfully.", deviceUuid);
  }
}
