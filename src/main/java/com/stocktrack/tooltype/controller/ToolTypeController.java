package com.stocktrack.tooltype.controller;

import com.stocktrack.tooltype.dto.request.ToolTypeRequestDTO;
import com.stocktrack.tooltype.dto.response.ToolTypeResponseDTO;
import com.stocktrack.tooltype.service.ToolTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tool-types")
@RequiredArgsConstructor
public class ToolTypeController {

    private final ToolTypeService toolTypeService;

    @GetMapping
    public List<ToolTypeResponseDTO> findAll(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean requiresCalibration) {
        return toolTypeService.findAll(category, requiresCalibration);
    }

    @GetMapping("/{id}")
    public ToolTypeResponseDTO findById(@PathVariable Long id) {
        return toolTypeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolTypeResponseDTO create(@Valid @RequestBody ToolTypeRequestDTO dto) {
        return toolTypeService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolTypeResponseDTO update(@PathVariable Long id, @Valid @RequestBody ToolTypeRequestDTO dto) {
        return toolTypeService.update(id, dto);
    }

    // DELETE /tool-types/{id} pendente — depende da validação de Tool vinculada,
    // que só existe quando o módulo Tool for implementado (ver 03-Business-Rules.md,
    // seção Tipo de Ferramenta > Exclusão).
}
