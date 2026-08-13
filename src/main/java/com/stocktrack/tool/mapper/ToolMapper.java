package com.stocktrack.tool.mapper;

import com.stocktrack.location.entity.Location;
import com.stocktrack.tool.dto.request.ToolCreateRequestDTO;
import com.stocktrack.tool.dto.response.ToolResponseDTO;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tooltype.entity.ToolType;
import com.stocktrack.tool.dto.response.ToolResponseDTO.LocationSummaryDTO;
import com.stocktrack.tool.dto.response.ToolResponseDTO.ToolTypeSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ToolMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastCalibrationDate", ignore = true)
    @Mapping(target = "nextCalibrationDate", ignore = true)
    @Mapping(target = "toolType", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tool toEntity(ToolCreateRequestDTO dto);

    @Mapping(target = "calibrationOverdue", source = "calibrationOverdue")
    ToolResponseDTO toResponseDTO(Tool tool, boolean calibrationOverdue);

    ToolTypeSummaryDTO toToolTypeSummaryDTO(ToolType toolType);

    LocationSummaryDTO toLocationSummaryDTO(Location location);
}
