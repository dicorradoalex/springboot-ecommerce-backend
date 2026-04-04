package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID id,

        @NotBlank(message = "Il nome è obbligatorio")
        String name,

        @NotNull()
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal price,

        String description,

        @NotNull(message = "La categoria è obbligatoria")
        CategoryDTO category) {

}
