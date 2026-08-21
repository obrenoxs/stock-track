package com.stocktrack.auditlog.mapper;

import com.stocktrack.auditlog.dto.response.AuditLogResponseDTO;
import com.stocktrack.auditlog.dto.response.AuditLogResponseDTO.ToolSummaryDTO;
import com.stocktrack.auditlog.dto.response.AuditLogResponseDTO.UserSummaryDTO;
import com.stocktrack.auditlog.entity.AuditLog;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuditLogMapper {

    AuditLogResponseDTO toResponseDTO(AuditLog auditLog);

    ToolSummaryDTO toToolSummaryDTO(Tool tool);

    UserSummaryDTO toUserSummaryDTO(User user);
}
