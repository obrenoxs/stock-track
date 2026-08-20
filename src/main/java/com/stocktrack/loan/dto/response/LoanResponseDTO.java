package com.stocktrack.loan.dto.response;

import java.time.LocalDateTime;

public record LoanResponseDTO(
        Long id,
        String reason,
        String observation,
        LocalDateTime loanDate,
        LocalDateTime expectedReturnDate,
        LocalDateTime returnDate,
        boolean late,
        ToolSummaryDTO tool,
        UserSummaryDTO borrowedByUser,
        UserSummaryDTO returnedByUser,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record ToolSummaryDTO(Long id, String serialNumber, String toolTypeName) {
    }

    public record UserSummaryDTO(Long id, String name, String re) {
    }
}
