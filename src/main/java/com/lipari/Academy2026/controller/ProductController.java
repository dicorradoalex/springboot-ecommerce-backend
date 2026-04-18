package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.ProductDTO;
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
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id) {
        // Chiedo al service di recuperare il prodotto
        ProductDTO product = this.productService.getProduct(id);
        // Se tutto ok, restituisci lo stato 200 e il dato nel corpo del body
        return ResponseEntity.ok(product);
    }

    /**
     * Recupera la lista completa di tutti i prodotti a catalogo.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
