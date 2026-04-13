package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.OrderDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;

public interface OrderService {

    public OrderDTO createOrder(OrderRequestDTO orderRequestDto);
}
