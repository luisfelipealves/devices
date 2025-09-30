package com.example.devices.mapper;

    
import com.example.devices.dto.DeviceDTO;
import com.example.devices.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    List<DeviceDTO> toDtoList(List<Device> devices);

    DeviceDTO toDto(Device device);

    Device toEntity(DeviceDTO deviceDTO);
}
