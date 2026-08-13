package com.stocktrack.tool.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ToolCreateRequestDTO(

        @NotNull(message = "Tipo de ferramenta é obrigatório")
        Long toolTypeId,

        @NotNull(message = "Localização obrigatória")
        Long locationId,

        @NotEmpty(message = "Pelo menos um numero de série deve ser informado")
        Set<@NotBlank String> serialNumbers
) {}
