package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.CartItemRequestDTO;
import com.lipari.Academy2026.dto.CartResponseDTO;
import com.lipari.Academy2026.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    // Dipendenze
    private final CartService cartService;

    /**
     * Recupera il carrello dell'utente corrente.
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        // Chiedo al service di recuperare i dati
        CartResponseDTO response = cartService.getCart();
        return ResponseEntity.ok(response);
    }

    /**
     * Aggiunge un prodotto al carrello tramite richiesta DTO.
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addItemToCart(@Valid @RequestBody CartItemRequestDTO request) {
        // Chiamo il service per l'inserimento
        CartResponseDTO response = cartService.addItemToCart(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Rimuove un articolo dal carrello tramite l'ID del prodotto.
     */
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@PathVariable UUID productId) {
        // Chiedo la rimozione al service
        CartResponseDTO response = cartService.removeItemFromCart(productId);
        return ResponseEntity.ok(response);
    }

    /**
     * Svuota completamente il carrello dell'utente corrente.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        // Chiamo il metodo di pulizia totale
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
