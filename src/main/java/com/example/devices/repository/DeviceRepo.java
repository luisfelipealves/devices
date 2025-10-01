package com.example.devices.repository;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {
  Optional<Device> findDeviceByUuid(UUID deviceUuid);

  Page<Device> findByBrand(String brand, Pageable pageable);

  Page<Device> findByState(DeviceState state, Pageable pageable);

  void deleteDeviceByUuid(UUID deviceUuid);

  boolean existsDeviceByUuid(UUID uuid);
}
