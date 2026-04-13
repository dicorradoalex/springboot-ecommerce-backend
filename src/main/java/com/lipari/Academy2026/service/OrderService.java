package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;

public interface OrderService {

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto);
}
