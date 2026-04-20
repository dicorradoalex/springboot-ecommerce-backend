package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza delle singole righe (item) del carrello.
 */
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

}
