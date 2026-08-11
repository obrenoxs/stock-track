package com.stocktrack.category.mapper;

import com.stocktrack.category.dto.request.CategoryRequestDTO;
import com.stocktrack.category.dto.response.CategoryResponseDTO;
import com.stocktrack.category.entity.Category;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    CategoryResponseDTO toResponseDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}
