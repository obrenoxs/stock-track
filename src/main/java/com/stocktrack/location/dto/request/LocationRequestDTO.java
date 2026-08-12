package com.stocktrack.location.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequestDTO(

        @NotBlank(message = "Corredor é obrigatório")
        @Size(max = 50, message = "Corredor deve ter no máximo 50 caracteres")
        String corridor,

        @NotBlank(message = "Prateleira é obrigatória")
        @Size(max = 50, message = "Prateleira deve ter no máximo 50 caracteres")
        String shelf,

        @NotBlank(message = "Gaveta é obrigatória")
        @Size(max = 50, message = "Gaveta deve ter no máximo 50 caracteres")
        String drawer
) {}
