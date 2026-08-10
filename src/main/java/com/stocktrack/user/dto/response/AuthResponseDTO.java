package com.stocktrack.user.dto.response;

public record AuthResponseDTO(
        String token,
        String tokenType,
        UserResponseDTO user
) {}
