package com.example.devices.entity;

import com.example.devices.enumerate.DeviceState;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devices", indexes = {
        @Index(name = "idx_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_brand", columnList = "brand"),
        @Index(name = "idx_state", columnList = "state")
})
@EntityListeners(AuditingEntityListener.class)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DeviceState state;

    @CreatedDate
    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @PrePersist
    private void generateUuid() {
        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

}
