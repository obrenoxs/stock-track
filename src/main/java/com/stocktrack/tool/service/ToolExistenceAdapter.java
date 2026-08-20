package com.stocktrack.tool.service;

import com.stocktrack.location.service.ToolExistencePort;
import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tool.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToolExistenceAdapter implements ToolExistencePort {

    private final ToolRepository toolRepository;

    @Override
    public boolean existsActiveToolForLocation(Long locationId) {
        return toolRepository.existsByLocation_IdAndStatusNot(locationId, ToolStatus.DISCARDED);
    }
}
