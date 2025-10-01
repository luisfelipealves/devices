package com.example.devices.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.service.DeviceService;
import jakarta.validation.ConstraintViolationException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
public class DeviceServiceIntegrationTest {

  @Autowired private DeviceService deviceService;

  @Test
  void createDevice_shouldReturnSavedDevice() {
    CreateDeviceDTO deviceDto = new CreateDeviceDTO("Test Device", "Test Brand", "AVAILABLE");
    DeviceDTO savedDevice = deviceService.createDevice(deviceDto);

    assertThat(savedDevice).isNotNull();
    assertThat(savedDevice.uuid()).isNotNull();
    assertThat(savedDevice.name()).isEqualTo("Test Device");
    assertThat(savedDevice.brand()).isEqualTo("Test Brand");
    assertThat(savedDevice.state()).isEqualTo("AVAILABLE");
    assertNotNull(savedDevice.creationTime());
  }

  @Test
  void updateDevice_shouldReturnUpdatedDevice() {
    CreateDeviceDTO initialDeviceDTO = new CreateDeviceDTO("Old Name", "Old Brand", "AVAILABLE");
    DeviceDTO savedDevice = deviceService.createDevice(initialDeviceDTO);

    UpdateDeviceDTO updateDeviceDTO =
        new UpdateDeviceDTO(savedDevice.uuid(), "New Name", "New Brand", "IN_USE");
    DeviceDTO updatedDevice = deviceService.updateDevice(updateDeviceDTO);

    assertThat(updatedDevice).isNotNull();
    assertThat(updatedDevice.uuid()).isEqualTo(savedDevice.uuid());
    assertThat(updatedDevice.name()).isEqualTo("New Name");
    assertThat(updatedDevice.brand()).isEqualTo("New Brand");
    assertThat(updatedDevice.state()).isEqualTo("IN_USE");
  }

  @Test
  void updateDevice_shouldThrowExceptionWhenInUseAndNameIsChanged() {
    CreateDeviceDTO initialDeviceDTO = new CreateDeviceDTO("Valid Name", "Valid Brand", "IN_USE");
    DeviceDTO savedDevice = deviceService.createDevice(initialDeviceDTO);

    UpdateDeviceDTO invalidUpdateDeviceDTO =
        new UpdateDeviceDTO(savedDevice.uuid(), "Valid Name", "New Brand", "IN_USE");

    ConstraintViolationException constraintViolationException =
        assertThrows(
            ConstraintViolationException.class,
            () -> deviceService.updateDevice(invalidUpdateDeviceDTO));

    assertEquals(
        "updateDevice.device: Name and brand properties cannot be updated if the device is in use",
        constraintViolationException.getMessage());
  }

  @Test
  void updateDevice_shouldThrowExceptionWhenDeviceDoesntExist() {
    UpdateDeviceDTO invalidUpdateDeviceDTO =
        new UpdateDeviceDTO(UUID.randomUUID().toString(), "Valid Name", "New Brand", "IN_USE");

    ConstraintViolationException constraintViolationException =
        assertThrows(
            ConstraintViolationException.class,
            () -> deviceService.updateDevice(invalidUpdateDeviceDTO));

    assertEquals(
        "updateDevice.device.uuid: Device not found", constraintViolationException.getMessage());
  }

  @Test
  void getDeviceByUuid_shouldReturnDevice() {
      CreateDeviceDTO initialDeviceDTO = new CreateDeviceDTO("Find Me", "Search Brand", "AVAILABLE");
    DeviceDTO savedDevice = deviceService.createDevice(initialDeviceDTO);

    DeviceDTO foundDevice = deviceService.getDeviceByUuid(UUID.fromString(savedDevice.uuid()));

    assertThat(foundDevice).isNotNull();
    assertThat(foundDevice.uuid()).isEqualTo(savedDevice.uuid());
    assertThat(foundDevice.name()).isEqualTo("Find Me");
  }

  @Test
  void getDeviceByUuid_shouldThrowExceptionWhenNotFound() {
    UUID nonExistentUuid = UUID.randomUUID();
    RuntimeException runtimeException =
        assertThrows(RuntimeException.class, () -> deviceService.getDeviceByUuid(nonExistentUuid));

    assertEquals("Device not found with UUID: " + nonExistentUuid, runtimeException.getMessage());
  }

  @Test
  void deleteDevice_shouldRemoveDevice() {
      CreateDeviceDTO initialDeviceDTO = new CreateDeviceDTO("Delete Me", "Delete Brand", "AVAILABLE");
    DeviceDTO savedDevice = deviceService.createDevice(initialDeviceDTO);

    deviceService.deleteDevice(savedDevice.uuid());

    RuntimeException runtimeException =
        assertThrows(
            RuntimeException.class,
            () -> deviceService.getDeviceByUuid(UUID.fromString(savedDevice.uuid())));

    assertEquals(
        "Device not found with UUID: " + savedDevice.uuid(), runtimeException.getMessage());
  }

  @Test
  void deleteDevice_shouldThrowExceptionWhenNotFound() {
    UUID nonExistentUuid = UUID.randomUUID();
    ConstraintViolationException constraintViolationException =
        assertThrows(
            ConstraintViolationException.class,
            () -> deviceService.deleteDevice(nonExistentUuid.toString()));

    assertEquals(
        "deleteDevice.deviceUuid: Device not found", constraintViolationException.getMessage());
  }

  @Test
  void deleteDevice_shouldThrowExceptionWhenDeviceIsInUse() {
      CreateDeviceDTO initialDeviceDTO = new CreateDeviceDTO("Delete Me", "Delete Brand", "IN_USE");
    DeviceDTO device = deviceService.createDevice(initialDeviceDTO);
    ConstraintViolationException constraintViolationException =
        assertThrows(
            ConstraintViolationException.class, () -> deviceService.deleteDevice(device.uuid()));

    assertEquals(
        "deleteDevice.deviceUuid: Device cannot be deleted if in use",
        constraintViolationException.getMessage());
  }
}
