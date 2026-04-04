package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    public ProductDTO createProduct(ProductDTO productDTO);

    public ProductDTO getProduct(String id);

    public void deleteProduct(String id);

    public ProductDTO modifyProduct(ProductDTO productDTO);

    public List<ProductDTO> getAllProducts();
}
