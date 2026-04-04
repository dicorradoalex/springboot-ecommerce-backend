package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.service.CategoryService;
import com.lipari.Academy2026.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor // Constructor Injection con Lombok
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    // final => iniettato automaticamente nel costruttore da Lombok
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable UUID id) {
        // Chiamo il service per ottenere il risultato
        CategoryDTO category = this.categoryService.getCategory(id);
        // Se tutto ok, restituisci lo stato 200 e il dato nel corpo del body
        return ResponseEntity.ok(category);
    }

    @PostMapping("/new")
    public ResponseEntity<CategoryDTO> newCategory(@RequestBody CategoryDTO CategoryDTO) {
        // Chiedo al Service di creare un nuovo prodotto
        CategoryDTO createdCategory = this.categoryService.createCategory(CategoryDTO);
        // Se tutto ok restituisco 201 + prodotto creato
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    // Delete con 204 no content
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable UUID id) {
        // Chiedi al service di eliminare il prodotto
        this.categoryService.deleteCategory(id);
        // Restituisci 204, no content
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/update")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO CategoryDTO) {
        // Chiedo al Service di modificare il prodotto
        CategoryDTO modifiedCategory = this.categoryService.updateCategory(CategoryDTO);
        // Se è tutto ok -> 200 (restituiendo il prodotto aggiornato)
        return ResponseEntity.ok(modifiedCategory);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = this.categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

}
