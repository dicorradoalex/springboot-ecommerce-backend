package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.order.OrderRequestDTO;
import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Gestisce la creazione e la visualizzazione degli ordini.
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    // Dipendenze
    private final OrderService orderService;

    /**
     * Crea un nuovo ordine manualmente (Buy Now).
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = this.orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Finalizza l'acquisto trasformando il carrello in un ordine.
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        OrderResponseDTO order = this.orderService.checkout();
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * Recupera la lista degli ordini dell'utente loggato.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders() {
        // Chiedo al Service di recuperare gli ordini dell'utente loggato.
        List<OrderResponseDTO> myOrders = this.orderService.getMyOrders();

        // Restituisco la lista con stato 200 OK
        return ResponseEntity.ok(myOrders);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable UUID id) {
            OrderResponseDTO canceledOrder = this.orderService.cancelOrder(id);
            return ResponseEntity.ok(canceledOrder);
    }


    // AREA ADMIN

    /**
     * Aggiorna lo stato di un ordine specifico.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus newStatus) {

        OrderResponseDTO updatedOrder = this.orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
}


