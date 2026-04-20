package com.lipari.Academy2026.dto.auth;

/**
 * DTO per la risposta all'autenticazione.
 * Restituisce il token JWT generato e i dati base dell'utente.
 */
public record AuthResponseDTO(
        String token,
        String email,
        String role
) {}