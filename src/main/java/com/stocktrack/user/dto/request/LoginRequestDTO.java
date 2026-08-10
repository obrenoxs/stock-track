package com.stocktrack.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "RE é obrigatório")
        String re,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}
