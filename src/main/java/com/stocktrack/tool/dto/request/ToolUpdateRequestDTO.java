package com.stocktrack.tool.dto.request;

import jakarta.validation.constraints.NotNull;

public record ToolUpdateRequestDTO(

        @NotNull(message = "Localização é obrigatória")
        Long locationId
) {}
