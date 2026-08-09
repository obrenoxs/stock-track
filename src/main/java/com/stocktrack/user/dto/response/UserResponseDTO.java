package com.stocktrack.user.dto.response;

import com.stocktrack.user.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String re,
        String area,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
