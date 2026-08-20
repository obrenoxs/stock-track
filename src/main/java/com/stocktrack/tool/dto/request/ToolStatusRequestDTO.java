package com.stocktrack.tool.dto.request;

import com.stocktrack.tool.enums.ToolStatus;
import jakarta.validation.constraints.NotNull;

public record ToolStatusRequestDTO(

        @NotNull(message = "Status é obrigatório")
        ToolStatus status
) {}
