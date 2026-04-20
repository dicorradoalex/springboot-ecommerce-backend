package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.cart.CartItemRequestDTO;
import com.lipari.Academy2026.dto.cart.CartResponseDTO;
import com.lipari.Academy2026.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Gestisce le operazioni relative al carrello acquisti dell'utente.
 */
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
        CartResponseDTO response = cartService.getCart();
        return ResponseEntity.ok(response);
    }

    /**
     * Aggiunge un prodotto al carrello tramite richiesta DTO.
     */
    @PostMapping
    public ResponseEntity<CartResponseDTO> addItemToCart(@Valid @RequestBody CartItemRequestDTO request) {
        CartResponseDTO response = cartService.addItemToCart(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Rimuove un articolo dal carrello tramite l'ID del prodotto.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@PathVariable UUID productId) {
        CartResponseDTO response = cartService.removeItemFromCart(productId);
        return ResponseEntity.ok(response);
    }

    /**
     * Svuota completamente il carrello dell'utente corrente.
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
