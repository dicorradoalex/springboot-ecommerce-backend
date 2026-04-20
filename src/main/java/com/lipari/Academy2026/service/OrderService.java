package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;
import com.lipari.Academy2026.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto);

    OrderResponseDTO checkout();

    OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus newStatus);

    OrderResponseDTO cancelOrder(UUID orderId);

    List<OrderResponseDTO> getMyOrders();
}