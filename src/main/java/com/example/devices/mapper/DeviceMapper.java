package com.example.devices.mapper;

    
import com.example.devices.dto.CreateDeviceDTO;
import com.example.devices.dto.DeviceDTO;
import com.example.devices.dto.UpdateDeviceDTO;
import com.example.devices.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    List<DeviceDTO> toDtoList(List<Device> devices);

    DeviceDTO toDto(Device device);
    UpdateDeviceDTO toUpdateDto(Device device);
    CreateDeviceDTO toCreateDto(Device device);

    Device toEntity(UpdateDeviceDTO deviceDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    Device toEntity(CreateDeviceDTO deviceDTO);

}
