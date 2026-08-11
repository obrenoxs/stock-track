package com.stocktrack.tooltype.dto.request;

import jakarta.validation.constraints.*;

import java.util.Set;

public record ToolTypeRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "Marca é obrigatória")
        @Size(max = 100, message = "Marca deve ter no máximo 100 caracteres")
        String brand,

        @NotBlank(message = "Modelo é obrigatório")
        @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
        String model,

        String description,

        @NotNull(message = "Estoque mínimo é obrigatório")
        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer minimumStock,

        @NotNull(message = "Indicação de calibração é obrigatória")
        Boolean requiresCalibration,

        @NotEmpty(message = "Pelo menos uma categoria deve ser associada")
        Set<Long> categoryIds
) {}
