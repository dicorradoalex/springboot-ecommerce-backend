package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository per la gestione della persistenza delle categorie di prodotti.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

}
