package com.stocktrack.auditlog.repository;

import com.stocktrack.auditlog.entity.AuditLog;
import com.stocktrack.auditlog.enums.ActionType;
import org.springframework.data.jpa.domain.Specification;

public class AuditLogSpecifications {

    private AuditLogSpecifications() {
    }

    public static Specification<AuditLog> hasTool(Long toolId) {
        return (root, query, cb) -> toolId == null ? cb.conjunction()
                : cb.equal(root.join("tool").get("id"), toolId);
    }

    public static Specification<AuditLog> hasUser(Long userId) {
        return (root, query, cb) -> userId == null ? cb.conjunction()
                : cb.equal(root.join("user").get("id"), userId);
    }

    public static Specification<AuditLog> hasActionType(ActionType actionType) {
        return (root, query, cb) -> actionType == null ? cb.conjunction()
                : cb.equal(root.get("actionType"), actionType);
    }
}
