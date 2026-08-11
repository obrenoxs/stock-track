package com.stocktrack.tooltype.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record ToolTypeResponseDTO(
        Long id,
        String name,
        String brand,
        String model,
        String description,
        int minimumStock,
        boolean requiresCalibration,
        int availableQuantity,
        Set<CategorySummaryDTO> categories,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record CategorySummaryDTO(Long id, String name){
    }
}
