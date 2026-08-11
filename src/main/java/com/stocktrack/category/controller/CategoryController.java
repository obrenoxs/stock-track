package com.stocktrack.category.controller;

import com.stocktrack.category.dto.request.CategoryRequestDTO;
import com.stocktrack.category.dto.response.CategoryResponseDTO;
import com.stocktrack.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public CategoryResponseDTO create(@Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public CategoryResponseDTO update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.update(id, dto);
    }

    // DELETE /categories/{id} pendente — depende da validação de ToolType vinculado,
    // que só existe quando o módulo ToolType for implementado (ver 03-Business-Rules.md,
    // seção Categoria > Exclusão).
}
