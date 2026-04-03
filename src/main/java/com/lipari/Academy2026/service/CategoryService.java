package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    public ProductDTO newProduct(ProductDTO productDTO);

    public ProductDTO getProduct(String id) throws Exception;

    public void deleteProduct(String id) throws Exception;

    public ProductDTO modifyProduct(ProductDTO productDTO) throws Exception;

    public List<ProductDTO> getAllProducts();
}
