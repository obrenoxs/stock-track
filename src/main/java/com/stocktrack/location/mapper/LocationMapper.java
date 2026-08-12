package com.stocktrack.location.mapper;

import com.stocktrack.location.dto.request.LocationRequestDTO;
import com.stocktrack.location.dto.response.LocationResponseDTO;
import com.stocktrack.location.entity.Location;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Location toEntity(LocationRequestDTO dto);

    LocationResponseDTO toResponseDTO(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(LocationRequestDTO dto, @MappingTarget Location location);
}
