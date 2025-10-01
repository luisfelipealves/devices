package com.example.devices.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.example.devices.enumerate.DeviceState;
import java.util.UUID;
import org.junit.jupiter.api.Test;

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
        Device device = Device.builder()
                .id(2L)
                .uuid(uuid)
                .name("Builder Test Device")
                .brand("Builder Test Brand")
                .state(DeviceState.AVAILABLE)
                .build();

        assertEquals(2L, device.getId());
        assertEquals(uuid, device.getUuid());
        assertEquals("Builder Test Device", device.getName());
        assertEquals("Builder Test Brand", device.getBrand());
        assertEquals(DeviceState.AVAILABLE, device.getState());
    }
}
