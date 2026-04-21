package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.category.CategoryRequestDTO;
import com.lipari.Academy2026.dto.category.CategoryResponseDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.service.category.CategoryService;
import com.lipari.Academy2026.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.UUID;

/**
 * Gestisce la visualizzazione delle categorie di prodotti.
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    /**
     * Recupera i dettagli di una singola categoria.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable UUID id) {
        CategoryResponseDTO category = this.categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Recupera la lista di tutte le categorie disponibili.
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categoriesList = this.categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesList);
    }

    /**
     * Recupera tutti i prodotti appartenenti a una specifica categoria.
     * Esempio: /api/category/{id}/products
     */
    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable UUID id,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        
        // Uso direttamente il ProductService passando l'ID della categoria e il pageable
        Page<ProductResponseDTO> products =
                this.productService.getProductsByCategoryId(id, pageable);
                
        return ResponseEntity.ok(products);
    }


    // AREA ADMIN

    /**
     * Crea una nuova categoria di prodotti.
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO createdCategory = this.categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Aggiorna il nome di una categoria esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {

        CategoryResponseDTO updatedCategory = this.categoryService.updateCategory(categoryRequestDTO, id);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Elimina una categoria dal sistema tramite ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id) {

        this.categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
