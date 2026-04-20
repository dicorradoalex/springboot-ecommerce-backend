package com.lipari.Academy2026.dto.user;

import java.util.UUID;

/**
 * DTO per la risposta dei dati del profilo utente.
 */
public record UserResponseDTO(
        UUID id,
        String email,
        String name,
        String surname,
        String address,
        String city,
        String country
) {}