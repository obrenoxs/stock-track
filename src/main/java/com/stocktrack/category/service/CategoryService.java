package com.stocktrack.category.service;

import com.stocktrack.category.dto.request.CategoryRequestDTO;
import com.stocktrack.category.dto.response.CategoryResponseDTO;
import com.stocktrack.category.entity.Category;
import com.stocktrack.category.mapper.CategoryMapper;
import com.stocktrack.category.repository.CategoryRepository;
import com.stocktrack.shared.exception.DuplicateResourceException;
import com.stocktrack.shared.exception.ResourceInUseException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ToolTypeExistencePort toolTypeExistencePort;

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        validateNameNotDuplicated(dto.name());

        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = getCategoryOrThrow(id);
        validateNameNotDuplicatedForOtherCategory(dto.name(), id);

        categoryMapper.updateEntityFromDto(dto, category);
        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponseDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getCategoryOrThrow(id);

        if (toolTypeExistencePort.existsForCategory(id)) {
            throw new ResourceInUseException("Categoria não pode ser excluída: existe Tipo de Ferramenta vinculado");
        }

        categoryRepository.delete(category);
    }

    private void validateNameNotDuplicated(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Categoria já cadastrada com nome: " + name);
        }
    }

    private void validateNameNotDuplicatedForOtherCategory(String name, Long id) {
        if (categoryRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("Categoria já cadastrada com nome: " + name);
        }
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));

    }
}
