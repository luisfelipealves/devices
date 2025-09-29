package com.example.devices.entity;

import com.example.devices.enumerate.DeviceState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void testDeviceCreation() {
        Device device = new Device();
        device.setId(1L);
        device.setName("Test Device");
        device.setBrand("Test Brand");

        assertEquals(1L, device.getId());
        assertEquals("Test Device", device.getName());
        assertEquals("Test Brand", device.getBrand());
    }

    @Test
    void testDeviceBuilder() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime creationTime = LocalDateTime.now();
        Device device = Device.builder()
                .id(2L)
                .uuid(uuid)
                .name("Builder Test Device")
                .brand("Builder Test Brand")
                .state(DeviceState.AVAILABLE)
                .creationTime(creationTime)
                .build();

        assertEquals(2L, device.getId());
        assertEquals(uuid, device.getUuid());
        assertEquals("Builder Test Device", device.getName());
        assertEquals("Builder Test Brand", device.getBrand());
        assertEquals(DeviceState.AVAILABLE, device.getState());
        assertEquals(creationTime, device.getCreationTime());
    }
}
