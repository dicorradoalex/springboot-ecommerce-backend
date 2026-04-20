package com.lipari.Academy2026.dto.category;

import java.util.UUID;

/**
 * DTO per la risposta con i dettagli di una categoria.
 */
public record CategoryResponseDTO(
        UUID id,
        String name
) {}