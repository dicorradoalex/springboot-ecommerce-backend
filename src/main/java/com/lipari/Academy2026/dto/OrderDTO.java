package com.lipari.Academy2026.dto;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(UUID id,
                       UserDTO user,
                       String status,
                       LocalDateTime orderTime,
                       List<OrderEntryDTO> entries) {
}
