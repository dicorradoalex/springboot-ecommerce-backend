package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * AREA ADMIN: Gestisce gli ordini lato admin
 */

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * Aggiorna lo stato di un ordine specifico.
     * (es. da CREATED a SHIPPED o DELIVERED).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus newStatus) {

        // Chiamo il service per aggiornare lo stato dell'ordine
        OrderResponseDTO updatedOrder = this.orderService.updateOrderStatus(id, newStatus);
        
        // Restituisco 200 OK con l'ordine aggiornato
        return ResponseEntity.ok(updatedOrder);
    }
}
