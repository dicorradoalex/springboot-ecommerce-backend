package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.ProductRequestDTO;
import com.lipari.Academy2026.dto.ProductResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponseDTO getProduct(UUID id);

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    void deleteProduct(UUID id);

    ProductResponseDTO updateProduct(ProductRequestDTO productRequestDTO, UUID id);

    List<ProductResponseDTO> getAllProducts();

    // Metodi di ricerca
    List<ProductResponseDTO> searchProductsByName(String name);

    List<ProductResponseDTO> getProductsByCategory(String categoryName);
}
