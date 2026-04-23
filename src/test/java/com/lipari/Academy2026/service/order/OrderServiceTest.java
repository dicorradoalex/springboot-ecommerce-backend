package com.lipari.Academy2026.service.order;

import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.entity.*;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.exception.InvalidOrderStateException;
import com.lipari.Academy2026.mapper.OrderMapper;
import com.lipari.Academy2026.repository.CartRepository;
import com.lipari.Academy2026.repository.OrderRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.stripe.StripeService;
import com.lipari.Academy2026.util.SecurityUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CartRepository cartRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private SecurityUtils securityUtils;
    @Mock private StripeService stripeService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserEntity currentUser;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        currentUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .email("user@test.it")
                .address("Via Roma 1")
                .city("Roma")
                .country("Italy")
                .build();
        
        product = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Prodotto Test")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Checkout: deve trasformare il carrello in ordine e scalare lo stock")
    void checkout_ShouldCreateOrderAndReduceStock() throws StripeException {
        // GIVEN
        CartEntity cart = CartEntity.builder().user(currentUser).items(new ArrayList<>()).build();
        CartItemEntity item = CartItemEntity.builder().cart(cart).product(product).quantity(2).build();
        cart.addItem(item);

        OrderEntity savedOrder = new OrderEntity();
        savedOrder.setId(UUID.randomUUID());

        Session stripeSession = mock(Session.class);
        when(stripeSession.getId()).thenReturn("sess_123");
        when(stripeSession.getUrl()).thenReturn("http://stripe.com/pay");

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(cartRepository.findByUser_Id(currentUser.getId())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);
        when(stripeService.createCheckoutSession(any())).thenReturn(stripeSession);
        
        // Mock per il mapper (record)
        OrderResponseDTO partialDto = new OrderResponseDTO(savedOrder.getId(), null, OrderStatus.PENDING_PAYMENT, LocalDateTime.now(), List.of(), new BigDecimal("200.00"), null, null);
        when(orderMapper.toDto(any())).thenReturn(partialDto);

        // WHEN
        OrderResponseDTO result = orderService.checkout();

        // THEN
        assertNotNull(result);
        assertEquals("sess_123", result.stripeSessionId());
        assertEquals(8, product.getStock()); // 10 - 2
        assertTrue(cart.getItems().isEmpty()); // Carrello svuotato
        verify(productRepository).save(product);
        verify(cartRepository).save(cart);
        verify(stripeService).createCheckoutSession(any());
    }

    @Test
    @DisplayName("Checkout: deve lanciare eccezione se l'utente non ha indirizzo")
    void checkout_ShouldThrowException_WhenAddressIsMissing() {
        // GIVEN
        currentUser.setAddress(null); // Rimuovo l'indirizzo
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);

        // WHEN & THEN
        assertThrows(InvalidOrderStateException.class, () -> orderService.checkout());
    }

    @Test
    @DisplayName("Annullamento: deve ripristinare lo stock")
    void cancelOrder_ShouldRestoreStock() {
        // GIVEN
        UUID orderId = UUID.randomUUID();
        OrderEntryEntity entry = OrderEntryEntity.builder().product(product).quantity(3).build();
        OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .user(currentUser)
                .status(OrderStatus.PENDING_PAYMENT)
                .entries(new ArrayList<>(List.of(entry)))
                .build();

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // WHEN
        orderService.cancelOrder(orderId);

        // THEN
        assertEquals(13, product.getStock()); // 10 + 3
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(productRepository).save(product);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Pagamento Fallito: deve ripristinare lo stock automaticamente")
    void handlePaymentFailure_ShouldRestoreStock() {
        // GIVEN
        String sessionId = "sess_fail";
        OrderEntryEntity entry = OrderEntryEntity.builder().product(product).quantity(5).build();
        OrderEntity order = OrderEntity.builder()
                .stripeSessionId(sessionId)
                .status(OrderStatus.PENDING_PAYMENT)
                .entries(List.of(entry))
                .build();

        when(orderRepository.findByStripeSessionId(sessionId)).thenReturn(Optional.of(order));

        // WHEN
        orderService.handlePaymentFailure(sessionId);

        // THEN
        assertEquals(15, product.getStock()); // 10 + 5
        assertEquals(OrderStatus.PAYMENT_FAILED, order.getStatus());
        verify(productRepository).save(product);
    }
}
