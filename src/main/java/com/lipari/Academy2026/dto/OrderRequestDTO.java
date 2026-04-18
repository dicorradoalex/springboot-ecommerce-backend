package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDTO(

        @NotEmpty(message = "L'ordine deve contenere almeno un prodotto")
        List<OrderEntryRequestDTO> entries) {
}


// Nota: L'utente che effettua l'ordine viene dedotto dal Token JWT per sicurezza