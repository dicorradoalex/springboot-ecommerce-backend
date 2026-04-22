package com.lipari.Academy2026.dto.order;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO completo per la risposta dei dati di un ordine.
 */
public record OrderResponseDTO(
        UUID id,
        UserResponseDTO user,
        OrderStatus status,
        LocalDateTime orderTime,
        List<OrderEntryResponseDTO> entries,
        BigDecimal total,
        String stripeSessionId,
        String paymentUrl
) {}
