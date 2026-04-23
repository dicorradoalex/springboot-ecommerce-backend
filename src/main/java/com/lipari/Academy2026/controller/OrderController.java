package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.order.OrderRequestDTO;
import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.service.order.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
     * Endpoint di atterraggio dopo un pagamento completato con successo su Stripe.
     */
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("session_id") String sessionId) {
        try {
            // 1. Chiediamo a Stripe il vero stato di questa sessione
            Session session = Session.retrieve(sessionId);

            // 2. Controlliamo se i soldi sono stati effettivamente prelevati
            if ("paid".equals(session.getPaymentStatus())) {
                return ResponseEntity.ok("Pagamento confermato! Il tuo ordine è in lavorazione. Session ID: " + sessionId);
            } else {
                // L'utente è arrivato qui per sbaglio (es. tasto indietro del browser) ma non ha pagato
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Attenzione: il pagamento non risulta completato. Controlla il tuo carrello e riprova.");
                // In un'app reale qui faresti un redirect (es. return "redirect:/cart")
            }

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante la verifica del pagamento con Stripe.");
        }
    }

    /**
     * Endpoint di atterraggio dopo che un utente ha annullato il pagamento su Stripe.
     */
    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Il pagamento è stato annullato. Puoi riprovare dal tuo carrello.");
    }

    /**
     * Permette di riprendere un pagamento in sospeso.
     */
    @GetMapping("/{id}/retry-payment")
    public ResponseEntity<Map<String, String>> retryPayment(@PathVariable UUID id) {
        try {
            // Recupera l'URL dal service
            String paymentUrl = this.orderService.getPaymentUrl(id);

            // Lo restituiamo in formato JSON per il frontend
            Map<String, String> response = new HashMap<>();
            response.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Recupera la lista degli ordini dell'utente loggato.
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getMyOrders(
            @PageableDefault(page = 0, size = 5, sort = "orderTime") Pageable pageable) {
        
        Page<OrderResponseDTO> myOrders = this.orderService.getMyOrders(pageable);
        return ResponseEntity.ok(myOrders);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable UUID id) {
            OrderResponseDTO canceledOrder = this.orderService.cancelOrder(id);
            return ResponseEntity.ok(canceledOrder);
    }


    // AREA ADMIN

    /**
     * Recupera la lista di tutti gli ordini presenti nel sistema (Riservato ADMIN).
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @PageableDefault(page = 0, size = 10, sort = "orderTime") Pageable pageable) {

        Page<OrderResponseDTO> allOrders = this.orderService.getAllOrders(pageable);
        return ResponseEntity.ok(allOrders);
    }

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


