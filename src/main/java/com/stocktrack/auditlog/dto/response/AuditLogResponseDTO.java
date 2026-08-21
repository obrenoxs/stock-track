package com.stocktrack.auditlog.dto.response;

import com.stocktrack.auditlog.enums.ActionType;

import java.time.LocalDateTime;

public record AuditLogResponseDTO(
        Long id,
        ActionType actionType,
        String reason,
        LocalDateTime createdAt,
        ToolSummaryDTO tool,
        UserSummaryDTO user
) {
    public record ToolSummaryDTO(Long id, String serialNumber) {
    }

    public record UserSummaryDTO(Long id, String name, String re) {
    }
}
