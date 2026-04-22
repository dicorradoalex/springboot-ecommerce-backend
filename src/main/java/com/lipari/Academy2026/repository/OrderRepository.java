package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza degli ordini effettuati dagli utenti.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Page<OrderEntity> findByUser_Id(UUID userId, Pageable pageable);

    /**
     * Recupera un ordine tramite l'ID della sessione di Stripe.
     */
    Optional<OrderEntity> findByStripeSessionId(String stripeSessionId);

}
