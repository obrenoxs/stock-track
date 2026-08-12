package com.stocktrack.location.dto.response;

import java.time.LocalDateTime;

public record LocationRequestDTO(
        Long id,
        String corridor,
        String shelf,
        String drawer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
