package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    public CategoryDTO createCategory(CategoryDTO CategoryDTO);

    public CategoryDTO getCategory(UUID id);

    public void deleteCategory(UUID id);

    public CategoryDTO updateCategory(CategoryDTO CategoryDTO);

    public List<CategoryDTO> getAllCategories();
}
