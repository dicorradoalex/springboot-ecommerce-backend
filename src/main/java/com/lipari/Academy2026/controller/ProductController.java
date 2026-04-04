package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor // Constructor Injection con Lombok
@RestController
@RequestMapping("/api/product")
public class ProductController {

    // final => iniettato automaticamente nel costruttore da Lombok
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {

        // Chiamo il service per ottenere il risultato
        ProductDTO product = this.productService.getProduct(id);
        // Se tutto ok, restituisci lo stato 200 e il dato nel corpo del body
        return ResponseEntity.ok(product);
    }

    @PostMapping("/new")
    public ResponseEntity<ProductDTO> newProduct(@RequestBody ProductDTO productDTO) {
        // Chiedo al Service di creare un nuovo prodotto
        ProductDTO createdProduct = this.productService.newProduct(productDTO);
        // Se tutto ok restituisco 201 + prodotto creato
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Delete con 204 no content
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable String id) {
            // Chiedi al service di eliminare il prodotto
            this.productService.deleteProduct(id);
            // Restituisci 204, no content
            return ResponseEntity.noContent().build();
    }

    /*

    // Delete con 200, restituisce l'item che ha cancellato come DTO
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable String id) {
        try {
            // Chiedi al service di eliminare il prodotto
            ProductDTO deleted = this.productService.deleteProduct(id);
            // Restituisci ok e il prodotto che ha cancellato
            return ResponseEntity.ok(deleted);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    */


    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        // Chiedo al Service di modificare il prodotto
        ProductDTO modifiedProduct = this.productService.modifyProduct(productDTO);
        // Se è tutto ok -> 200 (restituiendo il prodotto aggiornato)
        return ResponseEntity.ok(modifiedProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = this.productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

}
