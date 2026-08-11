package com.stocktrack.tooltype.repository;

import com.stocktrack.tooltype.entity.ToolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ToolTypeRepository extends JpaRepository<ToolType, Long>, JpaSpecificationExecutor<ToolType> {

    boolean existsByCategories_id(Long categoryId);
}
