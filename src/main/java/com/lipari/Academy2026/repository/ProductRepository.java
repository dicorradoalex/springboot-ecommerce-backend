package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    // Ricerca prodotti per nome (parziale e case-insensitive)
    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    // Ricerca prodotti appartenenti a una specifica categoria
    List<ProductEntity> findByCategory_Name(String categoryName);

}
