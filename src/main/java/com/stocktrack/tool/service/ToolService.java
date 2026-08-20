package com.stocktrack.tool.service;

import com.stocktrack.location.entity.Location;
import com.stocktrack.location.service.LocationService;
import com.stocktrack.shared.exception.BusinessRuleException;
import com.stocktrack.shared.exception.DuplicateResourceException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import com.stocktrack.tool.dto.request.ToolCalibrationRequestDTO;
import com.stocktrack.tool.dto.request.ToolCreateRequestDTO;
import com.stocktrack.tool.dto.request.ToolStatusRequestDTO;
import com.stocktrack.tool.dto.request.ToolUpdateRequestDTO;
import com.stocktrack.tool.dto.response.ToolResponseDTO;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tool.event.*;
import com.stocktrack.tool.mapper.ToolMapper;
import com.stocktrack.tool.repository.ToolRepository;
import com.stocktrack.tool.repository.ToolSpecifications;
import com.stocktrack.tooltype.entity.ToolType;
import com.stocktrack.tooltype.service.ToolTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    private final ToolTypeService toolTypeService;
    private final LocationService locationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ToolResponseDTO create(ToolCreateRequestDTO dto, Long performedByUserId) {
        validateSerialNumberNotDuplicated(dto.serialNumber());

        ToolType toolType = toolTypeService.findEntityById(dto.toolTypeId());
        Location location = locationService.findEntityById(dto.locationId());

        Tool tool = toolMapper.toEntity(dto);
        tool.setStatus(ToolStatus.AVAILABLE);
        tool.setToolType(toolType);
        tool.setLocation(location);

        Tool saved = toolRepository.save(tool);
        eventPublisher.publishEvent(new ToolCreatedEvent(saved.getId(), performedByUserId));

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<ToolResponseDTO> findAll(String name, Long categoryId, ToolStatus status,
                                         Long locationId, Long toolTypeId, Pageable pageable) {
        Specification<Tool> spec = Specification
                .where(ToolSpecifications.hasToolTypeNameContaining(name))
                .and(ToolSpecifications.hasCategory(categoryId))
                .and(ToolSpecifications.hasStatus(status))
                .and(ToolSpecifications.hasLocation(locationId))
                .and(ToolSpecifications.hasToolType(toolTypeId));

        return toolRepository.findAll(spec, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ToolResponseDTO findById(Long id) {
        return toResponseDTO(getToolOrThrow(id));
    }

    @Transactional(readOnly = true)
    public ToolResponseDTO findBySerialNumber(String serialNumber) {
        Tool tool = toolRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ferramenta não encontrada com número de série: " + serialNumber));
        return toResponseDTO(tool);
    }

    @Transactional
    public ToolResponseDTO updateLocation(Long id, ToolUpdateRequestDTO dto, Long performedByUserId) {
        Tool tool = getToolOrThrow(id);
        Location location = locationService.findEntityById(dto.locationId());

        tool.setLocation(location);
        Tool saved = toolRepository.save(tool);

        eventPublisher.publishEvent(new ToolLocationChangedEvent(saved.getId(), performedByUserId));
        return toResponseDTO(saved);
    }

    @Transactional
    public ToolResponseDTO changeStatus(Long id, ToolStatusRequestDTO dto, Long performedByUserId) {
        Tool tool = getToolOrThrow(id);
        ToolStatus current = tool.getStatus();
        ToolStatus target = dto.status();

        validateStatusTransition(current, target);

        tool.setStatus(target);

        if (target == ToolStatus.DISCARDED) {
            tool.setLocation(null);
            eventPublisher.publishEvent(new ToolDiscardedEvent(id, performedByUserId));
        } else if (target == ToolStatus.IN_MAINTENANCE) {
            eventPublisher.publishEvent(new ToolSentToMaintenanceEvent(id, performedByUserId));
        } else if (target == ToolStatus.AVAILABLE) {
            eventPublisher.publishEvent(new ToolReturnedFromMaintenanceEvent(id, performedByUserId));
        }

        Tool saved = toolRepository.save(tool);
        return toResponseDTO(saved);
    }

    @Transactional
    public ToolResponseDTO registerCalibration(Long id, ToolCalibrationRequestDTO dto, Long performedByUserId) {
        Tool tool = getToolOrThrow(id);
        ToolType toolType = tool.getToolType();

        if (!Boolean.TRUE.equals(toolType.isRequiresCalibration()) || tool.getNextCalibrationDate() == null) {
            throw new BusinessRuleException(
                    "Este tipo de ferramenta não exige controle de calibração");
        }

        tool.setLastCalibrationDate(dto.calibrationDate());
        tool.setNextCalibrationDate(
                dto.calibrationDate().plusMonths(toolType.getCalibrationIntervalMonths()));

        Tool saved = toolRepository.save(tool);
        eventPublisher.publishEvent(new ToolCalibrationRegisteredEvent(id, performedByUserId));

        return toResponseDTO(saved);
    }

    private void validateStatusTransition(ToolStatus current, ToolStatus target) {
        if (current == ToolStatus.DISCARDED) {
            throw new BusinessRuleException("Ferramenta descartada não pode mudar de status");
        }

        boolean validTransition = target == ToolStatus.DISCARDED
                || (current == ToolStatus.AVAILABLE && target == ToolStatus.IN_MAINTENANCE)
                || (current == ToolStatus.IN_MAINTENANCE && target == ToolStatus.AVAILABLE);

        if (!validTransition) {
            throw new BusinessRuleException(
                    "Transição de status inválida: " + current + " -> " + target);
        }
    }

    private void validateSerialNumberNotDuplicated(String serialNumber) {
        if (toolRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateResourceException("Número de série já cadastrado: " + serialNumber);
        }
    }

    private ToolResponseDTO toResponseDTO(Tool tool) {
        boolean overdue = isCalibrationOverdue(tool);
        return toolMapper.toResponseDTO(tool, overdue);
    }

    private boolean isCalibrationOverdue(Tool tool) {
        ToolType toolType = tool.getToolType();
        if (!Boolean.TRUE.equals(toolType.isRequiresCalibration()) || tool.getNextCalibrationDate() == null) {
            return false;
        }
        return tool.getNextCalibrationDate().isBefore(LocalDate.now());
    }

    private Tool getToolOrThrow(Long id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ferramenta não encontrada com id: " + id));
    }
}
