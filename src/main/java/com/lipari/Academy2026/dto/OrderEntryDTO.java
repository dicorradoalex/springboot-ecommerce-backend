package com.lipari.Academy2026.dto;

import com.lipari.Academy2026.entity.OrderEntity;
import com.lipari.Academy2026.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderEntryDTO(UUID id,
                            ProductDTO product,
                            OrderDTO order,
                            Integer quantity,
                            BigDecimal price) {
}
