package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.CartItemRequestDTO;
import com.lipari.Academy2026.dto.CartResponseDTO;

import java.util.UUID;

/**
 * Interfaccia per la gestione del carrello acquisti.
 */
public interface CartService {

    /**
     * Aggiunge un prodotto al carrello dell'utente corrente.
     */
    CartResponseDTO addItemToCart(CartItemRequestDTO request);

    /**
     * Recupera il carrello dell'utente corrente.
     */
    CartResponseDTO getCart();

    /**
     * Rimuove un articolo specifico dal carrello tramite ID prodotto.
     */
    CartResponseDTO removeItemFromCart(UUID productId);

    /**
     * Svuota completamente il carrello dell'utente corrente.
     */
    void clearCart();
}
