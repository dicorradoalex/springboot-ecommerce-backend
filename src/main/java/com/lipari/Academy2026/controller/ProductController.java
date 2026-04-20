package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.ProductResponseDTO;
import com.lipari.Academy2026.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * AREA USER: Permette le operazioni di base sui prodotti
 * da parte di un utente normale.
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
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID id) {
        // Chiedo al service di recuperare il prodotto
        ProductResponseDTO product = this.productService.getProduct(id);
        // Se tutto ok, restituisci lo stato 200 e il dato nel corpo del body
        return ResponseEntity.ok(product);
    }

    /**
     * Recupera la lista completa di tutti i prodotti a catalogo.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Cerca i prodotti per nome tramite un parametro di ricerca.
     * Esempio: /api/product/search?name=smartphone
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String name) {
        List<ProductResponseDTO> products = this.productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Filtra i prodotti in base alla loro categoria.
     * Esempio: /api/product/category?name=Informatica
     */
    @GetMapping("/category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@RequestParam(name = "name") String categoryName) {
        List<ProductResponseDTO> products = this.productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(products);
    }
}
