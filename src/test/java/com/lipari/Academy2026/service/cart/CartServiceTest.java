package com.lipari.Academy2026.service.cart;

import com.lipari.Academy2026.dto.cart.CartItemRequestDTO;
import com.lipari.Academy2026.dto.cart.CartResponseDTO;
import com.lipari.Academy2026.entity.CartEntity;
import com.lipari.Academy2026.entity.CartItemEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.exception.OutOfStockException;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.CartMapper;
import com.lipari.Academy2026.repository.CartRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per CartServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserEntity currentUser;
    private UUID productId;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        currentUser = UserEntity.builder().id(UUID.randomUUID()).build();
        productId = UUID.randomUUID();
        product = ProductEntity.builder()
                .id(productId)
                .name("Test Product")
                .price(new BigDecimal("10.00"))
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Aggiunta carrello: deve creare un nuovo item se non presente")
    void addItemToCart_ShouldAddNewItem_WhenNotPresent() {
        // GIVEN
        CartItemRequestDTO request = new CartItemRequestDTO(productId, 2);
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);

        // WHEN
        cartService.addItemToCart(request);

        // THEN
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Aggiunta carrello: deve aggiornare la quantità se il prodotto è già presente")
    void addItemToCart_ShouldUpdateQuantity_WhenAlreadyPresent() {
        // GIVEN
        CartItemRequestDTO request = new CartItemRequestDTO(productId, 3);
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();
        CartItemEntity existingItem = CartItemEntity.builder().cart(cart).product(product).quantity(2).build();
        cart.addItem(existingItem);

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);

        // WHEN
        cartService.addItemToCart(request);

        // THEN
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity()); // 2 esistenti + 3 nuovi
    }

    @Test
    @DisplayName("Aggiunta carrello: deve lanciare OutOfStockException se stock insufficiente")
    void addItemToCart_ShouldThrowOutOfStock_WhenStockIsLow() {
        // GIVEN
        CartItemRequestDTO request = new CartItemRequestDTO(productId, 15); // Chiediamo 15, stock è 10
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // WHEN & THEN
        assertThrows(OutOfStockException.class, () -> cartService.addItemToCart(request));
    }

    @Test
    @DisplayName("Rimozione carrello: deve rimuovere l'item se presente")
    void removeItemFromCart_ShouldRemoveItem() {
        // GIVEN
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();
        CartItemEntity item = CartItemEntity.builder().cart(cart).product(product).quantity(1).build();
        cart.addItem(item);

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);

        // WHEN
        cartService.removeItemFromCart(productId);

        // THEN
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Svuotamento carrello: deve pulire la lista degli item")
    void clearCart_ShouldEmptyTheList() {
        // GIVEN
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();
        cart.addItem(new CartItemEntity());

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));

        // WHEN
        cartService.clearCart();

        // THEN
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }
}
