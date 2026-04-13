package com.lipari.Academy2026.dto;

import com.lipari.Academy2026.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        UUID id,
        UserDTO user,
        OrderStatus status,
        LocalDateTime orderTime,
        List<OrderEntryResponseDTO> entries,
        BigDecimal total){
}

