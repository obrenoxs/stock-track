package com.stocktrack.tool.dto.response;

import com.stocktrack.tool.enums.ToolStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ToolResponseDTO(
        Long id,
        String serialNumber,
        ToolStatus status,
        LocalDate lastCalibrationDate,
        LocalDate nextCalibrationDate,
        boolean calibrationOverdue,
        ToolTypeSummaryDTO toolType,
        LocationSummaryDTO location,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record ToolTypeSummaryDTO(Long id, String name, String brand, String model) {
    }

    public record LocationSummaryDTO(Long id, String corridor, String shelf, String drawer) {
    }
}
