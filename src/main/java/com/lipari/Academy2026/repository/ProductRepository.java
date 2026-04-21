package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza del catalogo prodotti.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    /**
     * Ricerca i prodotti il cui nome contiene la stringa specificata (case-insensitive).
     */
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recupera l'elenco dei prodotti appartenenti a una categoria identificata dal suo ID.
     */
    Page<ProductEntity> findByCategory_Id(UUID categoryId, Pageable pageable);

    /**
     * Recupera l'elenco dei prodotti appartenenti a una categoria identificata dal nome (case-insensitive).
     */
    Page<ProductEntity> findByCategory_NameIgnoreCase(String categoryName, Pageable pageable);

}
