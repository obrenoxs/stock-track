package com.stocktrack.tooltype.mapper;

import com.stocktrack.category.entity.Category;
import com.stocktrack.tooltype.dto.request.ToolTypeRequestDTO;
import com.stocktrack.tooltype.dto.response.ToolTypeResponseDTO;
import com.stocktrack.tooltype.dto.response.ToolTypeResponseDTO.CategorySummaryDTO;
import com.stocktrack.tooltype.entity.ToolType;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ToolTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ToolType toEntity(ToolTypeRequestDTO dto);

    @Mapping(target = "availableQuantity", source = "availableQuantity")
    ToolTypeResponseDTO toResponseDTO(ToolType toolType, int availableQuantity);

    CategorySummaryDTO toCategorySummaryDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ToolTypeRequestDTO dto, @MappingTarget ToolType toolType);
}
