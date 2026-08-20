package com.stocktrack.location.service;

import com.stocktrack.location.dto.request.LocationRequestDTO;
import com.stocktrack.location.dto.response.LocationResponseDTO;
import com.stocktrack.location.entity.Location;
import com.stocktrack.location.mapper.LocationMapper;
import com.stocktrack.location.repository.LocationRepository;
import com.stocktrack.shared.exception.DuplicateResourceException;
import com.stocktrack.shared.exception.ResourceInUseException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ToolExistencePort toolExistencePort;

    @Transactional
    public LocationResponseDTO create(LocationRequestDTO dto) {
        validatePositionNotDuplicated(dto);

        Location location = locationMapper.toEntity(dto);
        Location saved = locationRepository.save(location);

        return locationMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<LocationResponseDTO> findAll() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponseDTO findById(Long id) {
        return locationMapper.toResponseDTO(getLocationOrThrow(id));
    }

    @Transactional
    public LocationResponseDTO update(LocationRequestDTO dto, Long id) {
        Location location = getLocationOrThrow(id);
        validatePositionNotDuplicatedForOtherLocation(dto, id);

        locationMapper.updateEntityFromDto(dto, location);
        Location saved = locationRepository.save(location);

        return locationMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Location findEntityById(Long id) {
        return getLocationOrThrow(id);
    }

    @Transactional
    public void delete(Long id) {
        Location location = getLocationOrThrow(id);

        if (toolExistencePort.existsActiveToolForLocation(id)) {
            throw new ResourceInUseException("Localização não pode ser excluída: existe Ferramenta ativa vinculada");
        }

        locationRepository.delete(location);
    }

    private void validatePositionNotDuplicated(LocationRequestDTO dto) {
        if (locationRepository.existsByCorridorAndShelfAndDrawer(dto.corridor(), dto.shelf(), dto.drawer())) {
            throw new DuplicateResourceException("Localização já cadastrada nessa posição");
        }

    }

    private void validatePositionNotDuplicatedForOtherLocation(LocationRequestDTO dto, Long id) {
        if (locationRepository.existsByCorridorAndShelfAndDrawerAndIdNot(
                dto.corridor(), dto.shelf(), dto.drawer(), id)) {
            throw new DuplicateResourceException("Localização já cadastrada nessa posição");
        }
    }

    private Location getLocationOrThrow(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma localização encontrada com id: " + id));
    }
}
