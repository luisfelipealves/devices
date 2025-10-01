package com.example.devices.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepo;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class DeviceServiceImplTest {

  private final DeviceMapper deviceMapper = DeviceMapper.INSTANCE;

  @Mock private DeviceRepo deviceRepo;
  @Mock private DeviceMapper deviceMapperMock;

  @InjectMocks private DeviceServiceImpl deviceService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createDevice() {
    Device device = new Device();
    device.setId(1L);
    device.setName("Test Device");
    device.setBrand("Test Brand");

    CreateDeviceDTO createDeviceDTO = deviceMapper.toCreateDto(device);
    DeviceDTO deviceDTO = deviceMapper.toDto(device);
    when(deviceMapperMock.toDto(device)).thenReturn(deviceDTO);
    when(deviceMapperMock.toEntity(createDeviceDTO)).thenReturn(device);
    when(deviceRepo.save(any(Device.class))).thenReturn(device);

    DeviceDTO createdDevice = deviceService.createDevice(createDeviceDTO);

    assertNotNull(createdDevice);
    assertEquals("Test Device", createdDevice.name());
    assertEquals("Test Brand", createdDevice.brand());

    verify(deviceRepo, times(1)).save(any(Device.class));
  }

  @Test
  void updateDevice() {
    UUID deviceUuid = UUID.randomUUID();

    Device existingDevice = new Device();
    existingDevice.setId(1L);
    existingDevice.setUuid(deviceUuid);
    existingDevice.setName("Old Name");
    existingDevice.setBrand("Old Brand");
    existingDevice.setState(DeviceState.AVAILABLE);

    Device changedDevice = new Device();
    changedDevice.setId(1L);
    changedDevice.setUuid(deviceUuid);
    changedDevice.setName("New Name");
    changedDevice.setBrand("New Brand");
    changedDevice.setState(DeviceState.AVAILABLE);

    UpdateDeviceDTO updatedDeviceDTO = deviceMapper.toUpdateDto(changedDevice);
    DeviceDTO changedDeviceDTO = deviceMapper.toDto(changedDevice);

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(existingDevice));
    when(deviceRepo.save(any(Device.class))).thenReturn(changedDevice);
    when(deviceMapperMock.toDto(changedDevice)).thenReturn(changedDeviceDTO);

    DeviceDTO result = deviceService.updateDevice(updatedDeviceDTO);

    assertNotNull(result);
    assertEquals(deviceUuid.toString(), result.uuid());
    assertEquals("New Name", result.name());
    assertEquals("New Brand", result.brand());

    verify(deviceRepo, times(1)).findDeviceByUuid(deviceUuid);
    verify(deviceRepo, times(1)).save(any(Device.class));
  }

  @Test
  void updateDeviceNotFound() {
    UUID deviceUuid = UUID.randomUUID();
    Device updatedDeviceDetails = new Device();
    updatedDeviceDetails.setId(1L);
    updatedDeviceDetails.setUuid(deviceUuid);
    updatedDeviceDetails.setName("New Name");
    updatedDeviceDetails.setBrand("New Brand");

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.empty());

    UpdateDeviceDTO updatedDeviceDTO = deviceMapper.toUpdateDto(updatedDeviceDetails);

    assertThrows(EntityNotFoundException.class, () -> deviceService.updateDevice(updatedDeviceDTO));

    verify(deviceRepo, times(1)).findDeviceByUuid(deviceUuid);
    verify(deviceRepo, never()).save(any(Device.class));
  }

  @Test
  void getDeviceByUuid() {
    UUID deviceUuid = UUID.randomUUID();
    Device device = new Device();
    device.setId(1L);
    device.setUuid(deviceUuid);
    device.setName("Test Device");
    device.setBrand("Test Brand");
    device.setState(DeviceState.AVAILABLE);

    DeviceDTO deviceDTO = deviceMapper.toDto(device);

    when(deviceMapperMock.toDto(device)).thenReturn(deviceDTO);
    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(device));

    DeviceDTO foundDevice = deviceService.getDeviceByUuid(deviceUuid);

    assertNotNull(foundDevice);
    assertEquals(deviceUuid.toString(), foundDevice.uuid());
    assertEquals("Test Device", foundDevice.name());
    assertEquals("Test Brand", foundDevice.brand());

    verify(deviceRepo, times(1)).findDeviceByUuid(deviceUuid);
  }

  @Test
  void getDeviceByIdNotFound() {
    UUID deviceUuid = UUID.randomUUID();

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> deviceService.getDeviceByUuid(deviceUuid));

    verify(deviceRepo, times(1)).findDeviceByUuid(deviceUuid);
  }

  @Test
  void deleteDevice() {
    UUID deviceUuid = UUID.randomUUID();
    Device device = new Device();
    device.setId(1L);
    device.setUuid(deviceUuid);

    doNothing().when(deviceRepo).delete(device);

    deviceService.deleteDevice(deviceUuid.toString());

    verify(deviceRepo, times(1)).deleteDeviceByUuid(device.getUuid());
  }

 @Test
  void getAllDevices() {
    Pageable pageable = PageRequest.of(0, 10);
    Device device1 = new Device();
    device1.setId(1L);
    device1.setName("Device 1");

    Device device2 = new Device();
    device2.setId(2L);
    device2.setName("Device 2");

    List<Device> deviceList = Arrays.asList(device1, device2);
    Page<Device> devicePage = new PageImpl<>(deviceList, pageable, deviceList.size());

    DeviceDTO device1DTO = deviceMapper.toDto(device1);
    DeviceDTO device2DTO = deviceMapper.toDto(device2);

    when(deviceRepo.findAll(pageable)).thenReturn(devicePage);
    when(deviceMapperMock.toDto(device1)).thenReturn(device1DTO);
    when(deviceMapperMock.toDto(device2)).thenReturn(device2DTO);

    Page<DeviceDTO> result = deviceService.getAllDevices(pageable);

    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals("Device 1", result.getContent().get(0).name());
    assertEquals("Device 2", result.getContent().get(1).name());

    verify(deviceRepo, times(1)).findAll(pageable);
  }

  @Test
  void getDevicesByBrand() {
    String brand = "Test Brand";
    Pageable pageable = PageRequest.of(0, 10);
    Device device1 = new Device();
    device1.setId(1L);
    device1.setName("Device 1");
    device1.setBrand(brand);

    Device device2 = new Device();
    device2.setId(2L);
    device2.setName("Device 2");
    device2.setBrand(brand);

    List<Device> deviceList = Arrays.asList(device1, device2);
    Page<Device> devicePage = new PageImpl<>(deviceList, pageable, deviceList.size());

    DeviceDTO device1DTO = deviceMapper.toDto(device1);
    DeviceDTO device2DTO = deviceMapper.toDto(device2);

    when(deviceRepo.findByBrand(brand, pageable)).thenReturn(devicePage);
    when(deviceMapperMock.toDto(device1)).thenReturn(device1DTO);
    when(deviceMapperMock.toDto(device2)).thenReturn(device2DTO);

    Page<DeviceDTO> result = deviceService.getDevicesByBrand(brand, pageable);

    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals("Device 1", result.getContent().get(0).name());
    assertEquals("Test Brand", result.getContent().get(0).brand());
    assertEquals("Device 2", result.getContent().get(1).name());
    assertEquals("Test Brand", result.getContent().get(1).brand());

    verify(deviceRepo, times(1)).findByBrand(brand, pageable);
  }

  @Test
  void getDevicesByState() {
    DeviceState state = DeviceState.IN_USE;
    Pageable pageable = PageRequest.of(0, 10);

    Device device = new Device();
    device.setId(2L);
    device.setName("Device 2");
    device.setState(state);

    List<Device> deviceList = List.of(device);
    Page<Device> devicePage = new PageImpl<>(deviceList, pageable, deviceList.size());

    DeviceDTO deviceDTO = deviceMapper.toDto(device);

    when(deviceRepo.findByState(state, pageable)).thenReturn(devicePage);
    when(deviceMapperMock.toDto(device)).thenReturn(deviceDTO);

    Page<DeviceDTO> result = deviceService.getDevicesByState(state.toString(), pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals("Device 2", result.getContent().getFirst().name());
    assertEquals(state.toString(), result.getContent().getFirst().state());

    verify(deviceRepo, times(1)).findByState(state, pageable);
  }
}
