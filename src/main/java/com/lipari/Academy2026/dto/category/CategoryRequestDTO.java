package com.lipari.Academy2026.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per la richiesta di creazione o aggiornamento di una categoria.
 */
public record CategoryRequestDTO(
        
        @NotBlank(message = "Il nome della categoria è obbligatorio")
        @Size(max = 100, message = "Il nome della categoria non può superare i 100 caratteri")
        String name
) {}