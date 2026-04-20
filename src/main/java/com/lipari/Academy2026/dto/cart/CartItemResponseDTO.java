package com.lipari.Academy2026.dto.cart;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO di dettaglio per una riga del carrello in risposta.
 */
public record CartItemResponseDTO(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
