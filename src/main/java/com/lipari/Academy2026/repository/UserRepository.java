package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository per la gestione della persistenza dei dati anagrafici e di accesso degli utenti.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Verifica l'esistenza di un utente tramite il suo indirizzo email.
     * Utilizzato principalmente durante la fase di registrazione.
     */
    boolean existsByEmail(String email);

    /**
     * Recupera un utente tramite il suo indirizzo email.
     * Utilizzato principalmente per il processo di autenticazione (login).
     */
    Optional<UserEntity> findByEmail(String email);
}
