package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza dei carrelli degli utenti.
 */
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    /**
     * Recupera il carrello associato a un utente tramite il suo ID.
     */
    Optional<CartEntity> findByUser_Id(UUID userId);
}
