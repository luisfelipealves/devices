package com.example.devices.repository;

import com.example.devices.entity.Device;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceRepoTest {
    private final DeviceRepo deviceRepo;

    @Autowired
    public DeviceRepoTest(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }


    @Test
    void testSaveDevice() {
        Device device = new Device();
        device.setDeviceUuid(java.util.UUID.randomUUID());
        device.setName("Test Device");
        device.setBrand("Test Brand");
        device.setCreationTime(LocalDateTime.now());

        Device savedDevice = deviceRepo.save(device);
        assertNotNull(savedDevice.getId());
        assertEquals("Test Device", savedDevice.getName());
    }

    @Test
    void testFindByUuid() {
        UUID deviceUuid = UUID.randomUUID();
        Device device = new Device();
        device.setDeviceUuid(deviceUuid);
        device.setName("Find Me");
        device.setBrand("Brand X");
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceRepo.save(device);

        Optional<Device> foundDevice = deviceRepo.findDeviceByDeviceUuid(deviceUuid);
        assertTrue(foundDevice.isPresent());
        assertEquals("Find Me", foundDevice.get().getName());
    }

    @Test
    void testFindAll() {
        deviceRepo.deleteAll(); // Clear existing data for consistent test
        Device device1 = new Device();
        device1.setDeviceUuid(java.util.UUID.randomUUID());
        device1.setName("Device 1");
        device1.setBrand("Brand A");
        device1.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device1);

        Device device2 = new Device();
        device2.setDeviceUuid(java.util.UUID.randomUUID());
        device2.setName("Device 2");
        device2.setBrand("Brand B");
        device2.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device2);

        List<Device> devices = deviceRepo.findAll();
        assertEquals(2, devices.size());
    }

    @Test
    void testDeleteByUuid() {
        UUID deviceUuid = UUID.randomUUID();
        Device device = new Device();
        device.setDeviceUuid(deviceUuid);
        device.setName("Delete Me");
        device.setBrand("Brand Y");
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceRepo.save(device);

        deviceRepo.deleteById(savedDevice.getId());
        Optional<Device> foundDevice = deviceRepo.findDeviceByDeviceUuid(deviceUuid);
        assertFalse(foundDevice.isPresent());
    }

    @Test
    void testUpdateDevice() {
        Device device = new Device();
        device.setDeviceUuid(java.util.UUID.randomUUID());
        device.setName("Original Name");
        device.setBrand("Original Brand");
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceRepo.save(device);

        savedDevice.setName("Updated Name");
        savedDevice.setBrand("Updated Brand");
        Device updatedDevice = deviceRepo.save(savedDevice);

        assertEquals("Updated Name", updatedDevice.getName());
        assertEquals("Updated Brand", updatedDevice.getBrand());
    }

}
