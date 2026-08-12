package com.stocktrack.tooltype.service;

import com.stocktrack.category.service.ToolTypeExistencePort;
import com.stocktrack.tooltype.repository.ToolTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToolTypeExistenceAdapter implements ToolTypeExistencePort {

    private final ToolTypeRepository toolTypeRepository;

    @Override
    public boolean existsForCategory(Long categoryId) {
        return toolTypeRepository.existsByCategories_id(categoryId);
    }
}
