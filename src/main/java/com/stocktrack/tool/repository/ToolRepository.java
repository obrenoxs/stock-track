package com.stocktrack.tool.repository;

import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tool.enums.ToolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool, Long>, JpaSpecificationExecutor<Tool> {

    boolean existsBySerialNumber(String serialNumber);

    Optional<Tool> findBySerialNumber(String serialNumber);

    boolean existsByToolType_Id(Long toolTypeId);

    boolean existsByLocation_IdAndStatusNot(Long locationId, ToolStatus status);

    long countByToolType_IdAndStatus(Long toolTypeId, ToolStatus status);
}
