package com.stocktrack.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "Área é obrigatória")
        @Size(max = 100, message = "Área deve ter no máximo 100 caracteres")
        String area,

        String currentPassword,

        @Size(min = 8, max = 100, message = "Nova senha deve ter entre 8 e 100 caracteres")
        String newPassword
) {}
