package com.example.devices.repository;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        device.setUuid(java.util.UUID.randomUUID());
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
        device.setUuid(deviceUuid);
        device.setName("Find Me");
        device.setBrand("Brand X");
        device.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device);

        Optional<Device> foundDevice = deviceRepo.findDeviceByUuid(deviceUuid);
        assertTrue(foundDevice.isPresent());
        assertEquals("Find Me", foundDevice.get().getName());
    }

    @Test
    void testFindAll() {
        deviceRepo.deleteAll(); // Clear existing data for consistent test
        Device device1 = new Device();
        device1.setUuid(java.util.UUID.randomUUID());
        device1.setName("Device 1");
        device1.setBrand("Brand A");
        device1.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device1);

        Device device2 = new Device();
        device2.setUuid(java.util.UUID.randomUUID());
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
        device.setUuid(deviceUuid);
        device.setName("Delete Me");
        device.setBrand("Brand Y");
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceRepo.save(device);

        deviceRepo.deleteById(savedDevice.getId());
        Optional<Device> foundDevice = deviceRepo.findDeviceByUuid(deviceUuid);
        assertFalse(foundDevice.isPresent());
    }

    @Test
    void testUpdateDevice() {
        Device device = new Device();
        device.setUuid(java.util.UUID.randomUUID());
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
    @Test
    void testFindByBrand() {
        deviceRepo.deleteAll();
        String brandName = "BrandForSearch";
        Device device1 = new Device();
        device1.setUuid(UUID.randomUUID());
        device1.setName("Device A");
        device1.setBrand(brandName);
        device1.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device1);

        Device device2 = new Device();
        device2.setUuid(UUID.randomUUID());
        device2.setName("Device B");
        device2.setBrand(brandName);
        device2.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device2);

        Device device3 = new Device();
        device3.setUuid(UUID.randomUUID());
        device3.setName("Device C");
        device3.setBrand("Other Brand");
        device3.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Device> devicesByBrand = deviceRepo.findByBrand(brandName, pageable);
        assertNotNull(devicesByBrand);
        assertEquals(2, devicesByBrand.getTotalElements());
        assertTrue(devicesByBrand.getContent().stream().allMatch(device -> device.getBrand().equals(brandName)));
    }

    @Test
    void testFindByState() {
        deviceRepo.deleteAll();
        Device device1 = new Device();
        device1.setUuid(UUID.randomUUID());
        device1.setName("Device A");
        device1.setBrand("Brand A");
        device1.setState(DeviceState.AVAILABLE);
        device1.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device1);

        Device device2 = new Device();
        device2.setUuid(UUID.randomUUID());
        device2.setName("Device B");
        device2.setBrand("Brand B");
        device2.setState(DeviceState.AVAILABLE);
        device2.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device2);

        Device device3 = new Device();
        device3.setUuid(UUID.randomUUID());
        device3.setName("Device C");
        device3.setBrand("Brand C");
        device3.setState(DeviceState.INACTIVE);
        device3.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Device> activeDevices = deviceRepo.findByState(DeviceState.AVAILABLE, pageable);
        assertNotNull(activeDevices);
        assertEquals(2, activeDevices.getTotalElements());
        assertTrue(activeDevices.getContent().stream().allMatch(device -> device.getState().equals(DeviceState.AVAILABLE)));

        Page<Device> inactiveDevices = deviceRepo.findByState(DeviceState.INACTIVE, pageable);
        assertNotNull(inactiveDevices);
        assertEquals(1, inactiveDevices.getTotalElements());
        assertTrue(inactiveDevices.getContent().stream().allMatch(device -> device.getState().equals(DeviceState.INACTIVE)));
    }

    @Test
    @Transactional
    void testDeleteDeviceByUuid() {
        UUID deviceUuidToDelete = UUID.randomUUID();
        Device device = new Device();
        device.setUuid(deviceUuidToDelete);
        device.setName("Device to Delete by UUID");
        device.setBrand("Brand D");
        device.setCreationTime(LocalDateTime.now());
        deviceRepo.save(device);

        Optional<Device> foundBeforeDelete = deviceRepo.findDeviceByUuid(deviceUuidToDelete);
        assertTrue(foundBeforeDelete.isPresent());

        deviceRepo.deleteDeviceByUuid(deviceUuidToDelete);

        Optional<Device> foundAfterDelete = deviceRepo.findDeviceByUuid(deviceUuidToDelete);
        assertFalse(foundAfterDelete.isPresent());
    }

}
