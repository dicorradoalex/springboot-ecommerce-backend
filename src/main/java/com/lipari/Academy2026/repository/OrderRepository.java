package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza degli ordini effettuati dagli utenti.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    /**
     * Recupera lo storico degli ordini associati a uno specifico utente.
     */
    List<OrderEntity> findByUser_Id(UUID userId);

}
