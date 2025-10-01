package com.example.devices.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import com.example.devices.repository.DeviceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Device device1;

    @BeforeEach
    void setUp() {
        deviceRepo.deleteAll();

        device1 = new Device();
        device1.setName("Samsung Galaxy S23");
        device1.setBrand("Samsung");
        device1.setState(DeviceState.AVAILABLE);
        device1.setCreationTime(LocalDateTime.now());

        Device device2 = new Device();
        device2.setName("iPhone 15");
        device2.setBrand("Apple");
        device2.setState(DeviceState.IN_USE);
        device2.setCreationTime(LocalDateTime.now());
        
        deviceRepo.save(device1);
        deviceRepo.save(device2);
    }

    @Test
    void createDevice() throws Exception {
        CreateDeviceDTO createDeviceDTO = new CreateDeviceDTO("Test Device", "Test Brand", "AVAILABLE");

        mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDeviceDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Device")))
                .andExpect(jsonPath("$.brand", is("Test Brand")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")));
    }

    @Test
    void updateDevice() throws Exception {
        UpdateDeviceDTO updateDeviceDTO = new UpdateDeviceDTO(device1.getUuid().toString(), "Updated Name", "Updated Brand", "IN_USE");

        mockMvc.perform(put("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDeviceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.brand", is("Updated Brand")))
                .andExpect(jsonPath("$.state", is("IN_USE")));
    }
    
    @Test
    void getDeviceByUuid() throws Exception {
        mockMvc.perform(get("/api/v1/devices/{uuid}", device1.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(device1.getUuid().toString())))
                .andExpect(jsonPath("$.name", is("Samsung Galaxy S23")));
    }

    @Test
    void getAllDevices() throws Exception {
        mockMvc.perform(get("/api/v1/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void getDevicesByBrand() throws Exception {
        mockMvc.perform(get("/api/v1/devices/brand/{brand}", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Samsung Galaxy S23")));
    }

    @Test
    void getDevicesByState() throws Exception {
        mockMvc.perform(get("/api/v1/devices/state/{state}", "IN_USE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("iPhone 15")));
    }

    @Test
    void deleteDevice() throws Exception {
        mockMvc.perform(delete("/api/v1/devices/{uuid}", device1.getUuid().toString()))
                .andExpect(status().isNoContent());
    }
}
