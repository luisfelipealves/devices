package com.example.devices.entity;

import com.example.devices.enumerate.DeviceState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID deviceUuid;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DeviceState state;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

}
