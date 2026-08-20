package com.stocktrack.loan.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LoanCreateRequestDTO(

        @NotNull(message = "Ferramenta é obrigatória")
        Long toolId,

        @NotBlank(message = "Motivo é obrigatório")
        @Size(max = 255, message = "Motivo deve ter no máximo 255 caracteres")
        String reason,

        @Future(message = "Data prevista de devolução deve ser no futuro")
        LocalDateTime expectedReturnDate
) {}
