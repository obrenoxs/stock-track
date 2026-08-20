package com.stocktrack.tooltype.service;

import com.stocktrack.category.entity.Category;
import com.stocktrack.category.service.CategoryService;
import com.stocktrack.shared.exception.BusinessRuleException;
import com.stocktrack.shared.exception.ResourceInUseException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import com.stocktrack.tooltype.dto.request.ToolTypeRequestDTO;
import com.stocktrack.tooltype.dto.response.ToolTypeResponseDTO;
import com.stocktrack.tooltype.entity.ToolType;
import com.stocktrack.tooltype.mapper.ToolTypeMapper;
import com.stocktrack.tooltype.repository.ToolTypeRepository;
import com.stocktrack.tooltype.repository.ToolTypeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ToolTypeService {

    private final ToolTypeRepository toolTypeRepository;
    private final ToolTypeMapper toolTypeMapper;
    private final CategoryService categoryService;
    private final ToolInventoryPort toolInventoryPort;

    @Transactional
    public ToolTypeResponseDTO create(ToolTypeRequestDTO dto) {
        validateCalibrationInterval(dto);

        ToolType toolType = toolTypeMapper.toEntity(dto);
        toolType.setCategories(resolveCategories(dto.categoryIds()));

        ToolType saved = toolTypeRepository.save(toolType);
        return toResponseDTO(saved);
    }

    @Transactional
    public ToolTypeResponseDTO update(Long id, ToolTypeRequestDTO dto) {
        validateCalibrationInterval(dto);

        ToolType toolType = getToolTypeOrThrow(id);

        toolTypeMapper.updateEntityFromDto(dto, toolType);
        toolType.setCategories(resolveCategories(dto.categoryIds()));

        ToolType saved = toolTypeRepository.save(toolType);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ToolTypeResponseDTO> findAll(Long categoryId, Boolean requiresCalibration) {
        Specification<ToolType> spec = Specification
                .where(ToolTypeSpecifications.hasCategory(categoryId))
                .and(ToolTypeSpecifications.requiresCalibration(requiresCalibration));

        return toolTypeRepository.findAll(spec).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ToolTypeResponseDTO findById(Long id) {
        ToolType toolType = getToolTypeOrThrow(id);
        return toResponseDTO(toolType);
    }

    @Transactional(readOnly = true)
    public ToolType findEntityById(Long id) {
        return getToolTypeOrThrow(id);
    }

    @Transactional
    public void delete(Long id) {
        ToolType toolType = getToolTypeOrThrow(id);

        if (toolInventoryPort.existsForToolType(id)) {
            throw new ResourceInUseException("Tipo de ferramenta não pode ser excluído: existe Ferramenta vinculada");
        }
    }

    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        List<Category> found = categoryService.findEntitiesByIds(categoryIds);

        if(found.size() != categoryIds.size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias informadas não existem");
        }

        return Set.copyOf(found);
    }

    private ToolTypeResponseDTO toResponseDTO(ToolType toolType) {
        long availableQuantity = toolInventoryPort.countAvailableForToolType(toolType.getId());
        return toolTypeMapper.toResponseDTO(toolType, (int) availableQuantity);
    }

    private ToolType getToolTypeOrThrow(Long id) {
        return toolTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de ferramenta não encontrada com id: " + id));
    }

    private void validateCalibrationInterval(ToolTypeRequestDTO dto) {
        if (Boolean.TRUE.equals(dto.requiresCalibration()) && dto.calibrationIntervalMonths() == null) {
            throw new BusinessRuleException(
                    "Intervalo de calibração é obrigatório quando requiresCalibration é true");
        }
    }
}
