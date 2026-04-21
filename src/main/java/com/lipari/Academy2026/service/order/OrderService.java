package com.lipari.Academy2026.service.order;

import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.dto.order.OrderRequestDTO;
import com.lipari.Academy2026.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaccia per la gestione del ciclo di vita degli ordini.
 */
public interface OrderService {


    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto);
    OrderResponseDTO checkout();
    OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus newStatus);
    OrderResponseDTO cancelOrder(UUID orderId);
    Page<OrderResponseDTO> getMyOrders(Pageable pageable);
}
