package com.stocktrack.tool.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ToolCreateRequestDTO(

        @NotNull(message = "Tipo de ferramenta é obrigatório")
        Long toolTypeId,

        @NotNull(message = "Localização obrigatória")
        Long locationId,

        @NotBlank(message = "Número de série é obrigatório")
        @Size(max = 100, message = "Numero de série deve ter no máximo 100 caracteres")
        String serialNumber
) {}
