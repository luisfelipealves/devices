package com.example.devices.repository;

import com.example.devices.entity.Device;
import com.example.devices.enumerate.DeviceState;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepo extends JpaRepository<Device, Long> {
  Optional<Device> findDeviceByUuid(UUID deviceUuid);

  List<Device> findByBrand(String brand);

  List<Device> findByState(DeviceState state);

  void deleteDeviceByUuid(UUID deviceUuid);
}
