package com.lipari.Academy2026.service.cart;

import com.lipari.Academy2026.dto.cart.CartItemRequestDTO;
import com.lipari.Academy2026.dto.cart.CartResponseDTO;

import java.util.UUID;

/**
 * Interfaccia per la gestione del carrello acquisti.
 */
public interface CartService {

    CartResponseDTO addItemToCart(CartItemRequestDTO request);
    CartResponseDTO getCart();
    CartResponseDTO removeItemFromCart(UUID productId);
    void clearCart();
}
