package com.stocktrack.tool.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ToolCalibrationRequestDTO(

        @NotNull(message = "Data de calibração é obrigatória")
        @PastOrPresent(message = "Data de calibração não pode ser no futuro")
        LocalDate calibrationDate
) {}
