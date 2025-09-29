package com.example.devices.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DeviceServiceImplTest {

  @Mock private DeviceRepo deviceRepo;

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

    when(deviceRepo.save(any(Device.class))).thenReturn(device);

    Device createdDevice = deviceService.createDevice(device);

    assertNotNull(createdDevice);
    assertEquals(1L, createdDevice.getId());
    assertEquals("Test Device", createdDevice.getName());
    assertEquals("Test Brand", createdDevice.getBrand());

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

    Device updatedDeviceDetails = new Device();
    updatedDeviceDetails.setId(1L);
    updatedDeviceDetails.setUuid(deviceUuid);
    updatedDeviceDetails.setName("New Name");
    updatedDeviceDetails.setBrand("New Brand");

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(existingDevice));
    when(deviceRepo.save(existingDevice)).thenReturn(existingDevice);

    Device result = deviceService.updateDevice(updatedDeviceDetails);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("New Name", result.getName());
    assertEquals("New Brand", result.getBrand());

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

    assertThrows(EntityNotFoundException.class, () -> deviceService.updateDevice(updatedDeviceDetails));

    verify(deviceRepo, times(1)).findDeviceByUuid(deviceUuid);
    verify(deviceRepo, never()).save(any(Device.class));
  }

  @Test
  void getDeviceByIdFound() {
    UUID deviceUuid = UUID.randomUUID();
    Device device = new Device();
    device.setId(1L);
    device.setUuid(deviceUuid);
    device.setName("Test Device");
    device.setBrand("Test Brand");

    when(deviceRepo.findDeviceByUuid(deviceUuid)).thenReturn(Optional.of(device));

    Device foundDevice = deviceService.getDeviceByUuid(deviceUuid);

    assertNotNull(foundDevice);
    assertEquals(1L, foundDevice.getId());
    assertEquals(deviceUuid, foundDevice.getUuid());
    assertEquals("Test Device", foundDevice.getName());
    assertEquals("Test Brand", foundDevice.getBrand());

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

    deviceService.deleteDevice(deviceUuid);

    verify(deviceRepo, times(1)).deleteDeviceByUuid(device.getUuid());
  }

  @Test
  void getAllDevices() {
    Device device1 = new Device();
    device1.setId(1L);
    device1.setName("Device 1");

    Device device2 = new Device();
    device2.setId(2L);
    device2.setName("Device 2");

    when(deviceRepo.findAll()).thenReturn(java.util.Arrays.asList(device1, device2));

    java.util.List<Device> devices = deviceService.getAllDevices();

    assertNotNull(devices);
    assertEquals(2, devices.size());
    assertEquals("Device 1", devices.get(0).getName());
    assertEquals("Device 2", devices.get(1).getName());

    verify(deviceRepo, times(1)).findAll();
  }

  @Test
  void getDevicesByBrand() {
    String brand = "Test Brand";
    Device device1 = new Device();
    device1.setId(1L);
    device1.setName("Device 1");
    device1.setBrand(brand);

    Device device2 = new Device();
    device2.setId(2L);
    device2.setName("Device 2");
    device2.setBrand(brand);

    when(deviceRepo.findByBrand(brand)).thenReturn(java.util.Arrays.asList(device1, device2));

    java.util.List<Device> devices = deviceService.getDevicesByBrand(brand);

    assertNotNull(devices);
    assertEquals(2, devices.size());
    assertEquals("Device 1", devices.get(0).getName());
    assertEquals("Test Brand", devices.get(0).getBrand());
    assertEquals("Device 2", devices.get(1).getName());
    assertEquals("Test Brand", devices.get(1).getBrand());

    verify(deviceRepo, times(1)).findByBrand(brand);
  }

  @Test
  void getDevicesByState() {
    Device device1 = new Device();
    device1.setId(1L);
    device1.setName("Device 1");
    device1.setState(DeviceState.AVAILABLE);

    Device device2 = new Device();
    device2.setId(2L);
    device2.setName("Device 2");
    device2.setState(DeviceState.IN_USE);

    when(deviceRepo.findByState(DeviceState.IN_USE)).thenReturn(List.of(device2));

    java.util.List<Device> devices = deviceService.getDevicesByState(DeviceState.IN_USE.toString());

    assertNotNull(devices);
    assertEquals(1, devices.size());
    assertEquals("Device 2", devices.getFirst().getName());
    assertEquals(DeviceState.IN_USE, devices.getFirst().getState());

    verify(deviceRepo, times(1)).findByState(DeviceState.IN_USE);
  }

}
