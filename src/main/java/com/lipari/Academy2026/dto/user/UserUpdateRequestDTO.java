package com.lipari.Academy2026.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO per la richiesta di aggiornamento del profilo utente.
 */
public record UserUpdateRequestDTO(

        @NotBlank(message = "Il nome è obbligatorio")
        String name,

        @NotBlank(message = "Il cognome è obbligatorio")
        String surname,

        String address,
        String city,
        String country
) {}
