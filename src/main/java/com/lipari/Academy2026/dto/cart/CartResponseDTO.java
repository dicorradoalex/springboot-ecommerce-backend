package com.lipari.Academy2026.dto.cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO per la risposta completa del carrello con calcolo del totale.
 */
public record CartResponseDTO(
        List<CartItemResponseDTO> items,
        BigDecimal total
) {}