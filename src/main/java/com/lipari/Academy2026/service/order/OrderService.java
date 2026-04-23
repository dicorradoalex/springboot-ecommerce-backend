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
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    /**
     * Recupera l'URL di pagamento di Stripe per un ordine esistente.
     */
    String getPaymentUrl(UUID orderId);

    /**
     * Gestisce la conferma del pagamento da parte di Stripe.
     * @param sessionId ID della sessione di checkout di Stripe.
     */
    void handlePaymentSuccess(String sessionId);

    /**
     * Gestisce il fallimento del pagamento da parte di Stripe.
     * @param sessionId ID della sessione di checkout di Stripe.
     */
    void handlePaymentFailure(String sessionId);
}
