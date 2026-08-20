package com.stocktrack.tool.controller;

import com.stocktrack.tool.dto.request.ToolCalibrationRequestDTO;
import com.stocktrack.tool.dto.request.ToolCreateRequestDTO;
import com.stocktrack.tool.dto.request.ToolStatusRequestDTO;
import com.stocktrack.tool.dto.request.ToolUpdateRequestDTO;
import com.stocktrack.tool.dto.response.ToolResponseDTO;
import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tool.service.ToolService;
import com.stocktrack.user.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;

    @GetMapping
    public Page<ToolResponseDTO> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) ToolStatus status,
            @RequestParam(required = false) Long location,
            @RequestParam(required = false) Long toolTypeId,
            Pageable pageable) {
        return toolService.findAll(name, category, status, location, toolTypeId, pageable);
    }

    @GetMapping("/{id}")
    public ToolResponseDTO findById(@PathVariable Long id) {
        return toolService.findById(id);
    }

    @GetMapping("/search")
    public ToolResponseDTO findBySerialNumber(@RequestParam String serialNumber) {
        return toolService.findBySerialNumber(serialNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolResponseDTO create(
            @Valid @RequestBody ToolCreateRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return toolService.create(dto, principal.getUser().getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolResponseDTO updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody ToolUpdateRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return toolService.updateLocation(id, dto, principal.getUser().getId());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolResponseDTO changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ToolStatusRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return toolService.changeStatus(id, dto, principal.getUser().getId());
    }

    @PatchMapping("/{id}/calibration")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ToolResponseDTO registerCalibration(
            @PathVariable Long id,
            @Valid @RequestBody ToolCalibrationRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return toolService.registerCalibration(id, dto, principal.getUser().getId());
    }
}
