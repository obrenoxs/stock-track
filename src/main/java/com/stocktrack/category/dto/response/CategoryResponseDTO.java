package com.stocktrack.category.dto.response;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
