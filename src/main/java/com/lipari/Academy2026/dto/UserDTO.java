package com.lipari.Academy2026.dto;

import com.lipari.Academy2026.entity.OrderEntity;
import java.util.UUID;
import java.util.List;

public record UserDTO(UUID id,
                      String name,
                      String surname,
                      String address,
                      String city,
                      String country,
                      List<OrderDTO> orders) {
}
