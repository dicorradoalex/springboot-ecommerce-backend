package com.lipari.Academy2026.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO per l'inserimento di un prodotto all'interno di un ordine.
 */
public record OrderEntryRequestDTO(

        @NotNull(message = "L'ID del prodotto è obbligatorio")
        UUID productId,

        @NotNull(message = "La quantità è obbligatoria")
        @Positive(message = "La quantità deve essere maggiore di zero")
        Integer quantity
) {}