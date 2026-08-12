package com.stocktrack.location.controller;

import com.stocktrack.location.dto.request.LocationRequestDTO;
import com.stocktrack.location.dto.response.LocationResponseDTO;
import com.stocktrack.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationResponseDTO> findAll() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public LocationResponseDTO findById(@PathVariable Long id) {
        return locationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public LocationResponseDTO create(@Valid @RequestBody LocationRequestDTO dto) {
        locationService.create(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public LocationResponseDTO update(@Valid @RequestBody LocationRequestDTO dto, Long id) {
        return locationService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
