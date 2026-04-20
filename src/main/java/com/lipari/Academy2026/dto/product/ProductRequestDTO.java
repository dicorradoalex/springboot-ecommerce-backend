package com.lipari.Academy2026.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO per la richiesta di creazione o aggiornamento di un prodotto.
 */
public record ProductRequestDTO(

        @NotBlank(message = "Il nome del prodotto è obbligatorio")
        String name,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal price,

        String description,

        @NotNull(message = "La quantità disponibile è obbligatoria")
        @PositiveOrZero(message = "La quantità non può essere negativa")
        Integer stock,

        @NotNull(message = "L'ID della categoria è obbligatorio")
        UUID categoryId
) {}
