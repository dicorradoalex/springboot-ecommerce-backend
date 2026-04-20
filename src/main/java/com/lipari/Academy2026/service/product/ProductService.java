package com.lipari.Academy2026.service.product;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Interfaccia per la gestione del catalogo prodotti.
 */
public interface ProductService {


    ProductResponseDTO getProduct(UUID id);
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    void deleteProduct(UUID id);
    ProductResponseDTO updateProduct(ProductRequestDTO productRequestDTO, UUID id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> searchProductsByName(String name);
    List<ProductResponseDTO> getProductsByCategory(String categoryName);
}
