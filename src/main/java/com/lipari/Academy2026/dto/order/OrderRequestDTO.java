package com.lipari.Academy2026.dto.order;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO per la richiesta di creazione di un nuovo ordine.
 */
public record OrderRequestDTO(
        
        @NotEmpty(message = "L'ordine deve contenere almeno un prodotto")
        List<OrderEntryRequestDTO> items
) {}