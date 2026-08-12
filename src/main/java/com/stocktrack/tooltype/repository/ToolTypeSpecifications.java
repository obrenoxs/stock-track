package com.stocktrack.tooltype.repository;

import com.stocktrack.tooltype.entity.ToolType;
import org.springframework.data.jpa.domain.Specification;

public class ToolTypeSpecifications {

    private ToolTypeSpecifications() {
    }

    public static Specification<ToolType> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            query.distinct(true);
            return criteriaBuilder.equal(root.join("categories").get("id"), categoryId);
        };
    }

    public static Specification<ToolType> requiresCalibration(Boolean requiresCalibration) {
        return (root, query, criteriaBuilder) -> {
            if (requiresCalibration == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("requiresCalibration"), requiresCalibration);
        };
    }
}
