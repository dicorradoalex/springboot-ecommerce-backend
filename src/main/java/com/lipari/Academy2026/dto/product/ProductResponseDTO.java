package com.lipari.Academy2026.dto.product;

import com.lipari.Academy2026.dto.category.CategoryResponseDTO;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO per la risposta dei dati di un prodotto.
 */
public record ProductResponseDTO(
        UUID id,
        String name,
        BigDecimal price,
        String description,
        Integer stock,
        CategoryResponseDTO category
) {}