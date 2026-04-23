package com.lipari.Academy2026.service.stripe;

import com.lipari.Academy2026.entity.OrderEntity;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

/**
 * Test di Unità per StripeServiceImpl.
 * Utilizza MockedStatic per intercettare le chiamate statiche all'SDK di Stripe.
 */
@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    @InjectMocks
    private StripeServiceImpl stripeService;

    private MockedStatic<Session> mockedSession;

    @BeforeEach
    void setUp() {
        // Inizializziamo le proprietà @Value manualmente per il test di unità
        ReflectionTestUtils.setField(stripeService, "successUrl", "http://success.com");
        ReflectionTestUtils.setField(stripeService, "cancelUrl", "http://cancel.com");
        
        // Apriamo il mock statico per la classe Session di Stripe
        mockedSession = mockStatic(Session.class);
    }

    @AfterEach
    void tearDown() {
        // È fondamentale chiudere il mock statico per evitare conflitti con altri test
        mockedSession.close();
    }

    @Test
    @DisplayName("Creazione Sessione: deve generare correttamente i parametri per Stripe")
    void createCheckoutSession_ShouldReturnSession() throws StripeException {
        // GIVEN
        UserEntity user = UserEntity.builder().email("customer@test.it").build();
        ProductEntity product = ProductEntity.builder().name("Prodotto Pro").build();
        
        OrderEntryEntity entry = OrderEntryEntity.builder()
                .product(product)
                .price(new BigDecimal("19.99"))
                .quantity(1)
                .build();
        
        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID())
                .user(user)
                .entries(List.of(entry))
                .build();

        Session expectedSession = mock(Session.class);
        
        // Istruiamo il mock statico a restituire la nostra sessione finta
        mockedSession.when(() -> Session.create(any(SessionCreateParams.class)))
                .thenReturn(expectedSession);

        // WHEN
        Session result = stripeService.createCheckoutSession(order);

        // THEN
        assertNotNull(result);
        // Verifichiamo che la chiamata statica sia avvenuta
        mockedSession.verify(() -> Session.create(any(SessionCreateParams.class)));
    }
}
