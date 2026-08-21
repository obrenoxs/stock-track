package com.stocktrack.auditlog.service;

import com.stocktrack.auditlog.dto.response.AuditLogResponseDTO;
import com.stocktrack.auditlog.entity.AuditLog;
import com.stocktrack.auditlog.enums.ActionType;
import com.stocktrack.auditlog.mapper.AuditLogMapper;
import com.stocktrack.auditlog.repository.AuditLogRepository;
import com.stocktrack.auditlog.repository.AuditLogSpecifications;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tool.service.ToolService;
import com.stocktrack.user.entity.User;
import com.stocktrack.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final ToolService toolService;
    private final UserService userService;

    @Transactional
    public void record(ActionType actionType, Long toolId, Long userId, String reason) {
        Tool tool = toolService.findEntityById(toolId);
        User user = userService.findEntityById(userId);

        AuditLog auditLog = new AuditLog();
        auditLog.setActionType(actionType);
        auditLog.setReason(reason);
        auditLog.setTool(tool);
        auditLog.setUser(user);

        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponseDTO> findAll(Long toolId, Long userId, ActionType actionType, Pageable pageable) {
        Specification<AuditLog> spec = Specification
                .where(AuditLogSpecifications.hasTool(toolId))
                .and(AuditLogSpecifications.hasUser(userId))
                .and(AuditLogSpecifications.hasActionType(actionType));

        return auditLogRepository.findAll(spec, pageable)
                .map(auditLogMapper::toResponseDTO);

    }
}
