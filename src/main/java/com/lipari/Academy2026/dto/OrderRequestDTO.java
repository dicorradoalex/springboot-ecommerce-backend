package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(

        // userId -> da rivedere quando implementi la security
        @NotNull
        UUID userId,

        @NotEmpty
        List<OrderEntryRequestDTO> entries) {
}
