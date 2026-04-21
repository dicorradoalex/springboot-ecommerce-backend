package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.web.PageableDefault;
import java.util.UUID;


/**
 * Gestisce la visualizzazione e la ricerca dei prodotti a catalogo.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    // Dipendenze
    private final ProductService productService;

    /**
     * Recupera i dettagli di un singolo prodotto tramite il suo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(
            @PathVariable UUID id) {

        ProductResponseDTO product = this.productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Recupera la lista completa di tutti i prodotti a catalogo.
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        Page<ProductResponseDTO> products = this.productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Cerca i prodotti per nome tramite un parametro di ricerca.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam String name,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        Page<ProductResponseDTO> products = this.productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Filtra i prodotti in base alla loro categoria.
     */
    @GetMapping("/category")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @RequestParam(name = "name") String categoryName,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        Page<ProductResponseDTO> products = this.productService.getProductsByCategory(categoryName, pageable);
        return ResponseEntity.ok(products);
    }



    // AREA ADMIN

    /**
     * Crea un nuovo prodotto nel catalogo.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO createdProduct = this.productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Aggiorna i dati di un prodotto esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO modifiedProduct = this.productService.updateProduct(productRequestDTO, id);
        return ResponseEntity.ok(modifiedProduct);
    }

    /**
     * Elimina un prodotto dal catalogo tramite ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID id) {

        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
