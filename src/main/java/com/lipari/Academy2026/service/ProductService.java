package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.ProductDTO;


import java.util.List;
import java.util.UUID;

public interface ProductService {

    public ProductDTO createProduct(ProductDTO productDTO);

    public ProductDTO getProduct(UUID id);

    public void deleteProduct(UUID id);

    public ProductDTO updateProduct(ProductDTO productDTO);

    public List<ProductDTO> getAllProducts();
}
