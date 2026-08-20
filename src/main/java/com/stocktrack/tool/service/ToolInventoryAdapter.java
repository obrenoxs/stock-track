package com.stocktrack.tool.service;

import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tool.repository.ToolRepository;
import com.stocktrack.tooltype.service.ToolInventoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToolInventoryAdapter implements ToolInventoryPort {

    private final ToolRepository toolRepository;

    @Override
    public boolean existsForToolType(Long toolTypeId) {
        return toolRepository.existsByToolType_Id(toolTypeId);
    }

    @Override
    public long countAvailableForToolType(Long toolTypeId) {
        return toolRepository.countByToolType_IdAndStatus(toolTypeId, ToolStatus.AVAILABLE);
    }
}
