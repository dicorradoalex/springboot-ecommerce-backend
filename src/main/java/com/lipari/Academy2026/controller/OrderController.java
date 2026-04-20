package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.OrderRequestDTO;
import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = this.orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        OrderResponseDTO order = this.orderService.checkout();
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/my-orders")
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
}


