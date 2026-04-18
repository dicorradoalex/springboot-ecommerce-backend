package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * AREA ADMIN: Gestione Catalogo
 */

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * Crea un nuovo prodotto nel catalogo.
     */
    @PostMapping("/new")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = this.productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Aggiorna i dati di un prodotto esistente.
     */
    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO modifiedProduct = this.productService.updateProduct(productDTO);
        return ResponseEntity.ok(modifiedProduct);
    }

    /**
     * Elimina un prodotto dal catalogo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
