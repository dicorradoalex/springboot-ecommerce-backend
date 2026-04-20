package com.lipari.Academy2026.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO per l'aggiunta o l'aggiornamento di un prodotto nel carrello.
 */
public record CartItemRequestDTO(

        @NotNull(message = "L'ID del prodotto è obbligatorio")
        UUID productId,

        @NotNull(message = "La quantità è obbligatoria")
        @Positive(message = "La quantità deve essere maggiore di zero")
        Integer quantity
) {}