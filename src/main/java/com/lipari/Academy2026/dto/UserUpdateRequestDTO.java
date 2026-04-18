package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO dedicato all'aggiornamento del profilo utente.
 * NOTA DI SICUREZZA: Non contiene l'ID (preso dal Token) né l'Email (dato sensibile non modificabile qui).
 */
public record UserUpdateRequestDTO(
        
        @NotBlank(message = "Il nome non può essere vuoto")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Il cognome non può essere vuoto")
        @Size(max = 100)
        String surname,

        @Size(max = 200)
        String address,

        @Size(max = 100)
        String city,

        @Size(max = 50)
        String country
) {}
