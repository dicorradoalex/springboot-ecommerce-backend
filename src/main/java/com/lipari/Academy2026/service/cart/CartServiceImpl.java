package com.lipari.Academy2026.service.cart;

import com.lipari.Academy2026.dto.cart.CartItemRequestDTO;
import com.lipari.Academy2026.dto.cart.CartResponseDTO;
import com.lipari.Academy2026.entity.CartEntity;
import com.lipari.Academy2026.entity.CartItemEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.CartMapper;
import com.lipari.Academy2026.repository.CartRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementazione del servizio per la gestione del carrello acquisti dell'utente.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    // DIPENDENZE
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    /**
     * Aggiunge un prodotto al carrello dell'utente o ne aggiorna la quantità se già presente.
     */
    @Override
    @Transactional
    public CartResponseDTO addItemToCart(CartItemRequestDTO request) {

        // Recupero l'utente loggato
        UserEntity currentUser = getCurrentUser();

        // Recupero il carrello o lo creo se non esiste
        CartEntity cart = this.cartRepository.findByUser_Id(currentUser.getId())
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder()
                            .user(currentUser)
                            .items(new ArrayList<>())
                            .build();
                    return this.cartRepository.save(newCart);
                });

        // Recupero il prodotto dal database
        ProductEntity product = this.productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + request.productId()));

        // Verifico se il prodotto è già presente nel carrello
        CartItemEntity existingItem = null;
        for (CartItemEntity item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                existingItem = item;
                break;
            }
        }

        // Se esiste aggiorno la quantità, altrimenti aggiungo un nuovo elemento
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();
            cart.addItem(newItem);
        }

        // Salvo e restituisco il carrello aggiornato
        CartEntity savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    /**
     * Recupera il carrello associato all'utente attualmente autenticato.
     */
    @Override
    public CartResponseDTO getCart() {

        // Recupero l'utente loggato
        UserEntity currentUser = getCurrentUser();

        // Cerco il carrello nel database
        CartEntity cart = cartRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Il tuo carrello è vuoto."));

        return cartMapper.toDto(cart);
    }

    /**
     * Rimuove un elemento specifico dal carrello utilizzando l'ID del prodotto.
     */
    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(UUID productId) {

        // Recupero l'utente loggato
        UserEntity currentUser = getCurrentUser();

        // Cerco il carrello
        CartEntity cart = cartRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrello non trovato"));

        // Cerco l'elemento da rimuovere tra quelli presenti
        CartItemEntity itemToRemove = null;
        for (CartItemEntity item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove == null)
            throw new ResourceNotFoundException("Prodotto non presente nel carrello");

        // Rimuovo l'elemento e salvo
        cart.removeItem(itemToRemove);
        CartEntity updatedCart = cartRepository.save(cart);
        
        return cartMapper.toDto(updatedCart);
    }

    /**
     * Elimina tutti gli elementi presenti nel carrello dell'utente corrente.
     */
    @Override
    @Transactional
    public void clearCart() {

        // Recupero l'utente loggato
        UserEntity currentUser = getCurrentUser();

        // Cerco il carrello e svuoto la lista degli item se presente
        var cartOpt = cartRepository.findByUser_Id(currentUser.getId());
        if (cartOpt.isPresent()) {
            CartEntity cart = cartOpt.get();
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    /**
     * Metodo helper per recuperare l'utente attualmente autenticato nel sistema dal contesto di sicurezza.
     */
    private UserEntity getCurrentUser() {

        return (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
