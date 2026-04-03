package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    public CategoryDTO createCategory(CategoryDTO CategoryDTO);

    public CategoryDTO getCategory(UUID id) throws Exception;

    public void deleteCategory(UUID id) throws Exception;

    public CategoryDTO updateCategory(CategoryDTO CategoryDTO) throws Exception;

    public List<CategoryDTO> getAllCategories();
}
