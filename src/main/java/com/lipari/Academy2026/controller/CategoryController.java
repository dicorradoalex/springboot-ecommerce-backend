package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 *  AREA USER: Gestione delle categorie lato utente (normale).
 */

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Recupera i dettagli di una singola categoria.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable UUID id) {
        CategoryDTO category = this.categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Recupera la lista di tutte le categorie disponibili.
     */
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoriesList = this.categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesList);
    }
}
