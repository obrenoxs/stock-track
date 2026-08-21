package com.stocktrack.auditlog.controller;

import com.stocktrack.auditlog.dto.response.AuditLogResponseDTO;
import com.stocktrack.auditlog.enums.ActionType;
import com.stocktrack.auditlog.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public Page<AuditLogResponseDTO> findAll(
            @RequestParam(required = false) Long toolId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ActionType actionType,
            Pageable pageable) {
        return auditLogService.findAll(toolId, userId, actionType, pageable);
    }
}
