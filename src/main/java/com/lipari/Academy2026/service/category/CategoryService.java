package com.lipari.Academy2026.service.category;

import com.lipari.Academy2026.dto.category.CategoryRequestDTO;
import com.lipari.Academy2026.dto.category.CategoryResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Interfaccia per la gestione del catalogo delle categorie.
 */
public interface CategoryService {

    CategoryResponseDTO getCategory(UUID id);
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(UUID id);
    CategoryResponseDTO updateCategory(CategoryRequestDTO categoryRequestDTO, UUID id);
    List<CategoryResponseDTO> getAllCategories();
}
