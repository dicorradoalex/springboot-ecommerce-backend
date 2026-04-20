package com.lipari.Academy2026.dto.error;

import java.time.LocalDateTime;

/**
 * DTO standardizzato per le risposte di errore dell'applicazione.
 */
public record ErrorResponseDTO(
        int status,
        String message,
        LocalDateTime timestamp
) {}