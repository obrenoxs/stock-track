package com.stocktrack.tool.repository;

import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tool.enums.ToolStatus;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ToolSpecifications {

    private ToolSpecifications() {
    }

    public static Specification<Tool> hasToolTypeNameContaining(String namePart) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(namePart)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("toolType").get("name")),
                    "%" + namePart.toLowerCase() + "%");
        };
    }

    public static Specification<Tool> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            query.distinct(true);
            return criteriaBuilder.equal(
                    root.join("toolType").join("categories").get("id"), categoryId);
        };
    }

    public static Specification<Tool> hasStatus(ToolStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Tool> hasLocation(Long locationId) {
        return (root, query, criteriaBuilder) -> {
            if (locationId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("location").get("id"), locationId);
        };
    }

    public static Specification<Tool> hasToolType(Long toolTypeId) {
        return (root, query, criteriaBuilder) -> {
            if (toolTypeId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("toolType").get("id"), toolTypeId);
        };
    }
}
