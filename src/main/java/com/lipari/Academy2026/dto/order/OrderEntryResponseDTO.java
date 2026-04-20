package com.lipari.Academy2026.dto.order;

import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO per il dettaglio di una riga d'ordine in risposta.
 */
public record OrderEntryResponseDTO(
        UUID id,
        ProductResponseDTO product,
        Integer quantity,
        BigDecimal price,
        BigDecimal total
) {}