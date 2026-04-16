package com.lipari.Academy2026.repository;

import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    // Per la registrazione - Restituisce true/false per evitare duplicati
    boolean existsByEmail(String email);

    // Per il login: restituisce l'utente completo o un Optional vuoto se non esiste
    Optional<UserEntity> findByEmail(String email);
}

// Grazie a JpaRepository: save(), findById(), findAll(), deleteById()

