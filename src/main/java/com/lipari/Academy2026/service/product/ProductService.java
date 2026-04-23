package com.lipari.Academy2026.service.product;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interfaccia per la gestione del catalogo prodotti.
 */
public interface ProductService {


    ProductResponseDTO getProduct(UUID id);
    ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequestDTO);
    void deleteProduct(UUID id);
    ProductResponseDTO updateProduct(@Valid ProductRequestDTO productRequestDTO, UUID id);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    Page<ProductResponseDTO> searchProductsByName(String name, Pageable pageable);
    Page<ProductResponseDTO> getProductsByCategory(String categoryName, Pageable pageable);
    Page<ProductResponseDTO> getProductsByCategoryId(UUID categoryId, Pageable pageable);
}
