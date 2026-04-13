package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderEntryRequestDTO(

        @NotNull
        UUID productId,

        @Positive
        Integer quantity) {
}





