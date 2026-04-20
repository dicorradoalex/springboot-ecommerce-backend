package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.ProductEntity;
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
    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Recupera l'elenco dei prodotti appartenenti a una categoria identificata dal nome (case-insensitive).
     */
    List<ProductEntity> findByCategory_NameIgnoreCase(String categoryName);

}
