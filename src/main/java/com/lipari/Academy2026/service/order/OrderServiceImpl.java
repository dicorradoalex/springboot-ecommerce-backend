package com.lipari.Academy2026.service.order;

import com.lipari.Academy2026.dto.order.OrderEntryRequestDTO;
import com.lipari.Academy2026.dto.order.OrderRequestDTO;
import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.entity.*;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.exception.InvalidOrderStateException;
import com.lipari.Academy2026.exception.OutOfStockException;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.OrderMapper;
import com.lipari.Academy2026.repository.CartRepository;
import com.lipari.Academy2026.repository.OrderRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.stripe.StripeService;
import com.lipari.Academy2026.util.SecurityUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementazione del servizio per la gestione degli ordini d'acquisto.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    // DIPENDENZE
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final SecurityUtils securityUtils;
    private final StripeService stripeService;

    /**
     * Crea un nuovo ordine registrando le singole voci e aggiornando lo stock dei prodotti.
     */
    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto) {

        // Recupero l'utente loggato tramite SecurityUtils
        UserEntity currentUser = securityUtils.getCurrentUser();

        // Validazione dati di spedizione
        validateUserAddress(currentUser);

        // Inizializzo l'entità Ordine
        OrderEntity newOrder = OrderEntity.builder()
                .user(currentUser)
                .status(OrderStatus.PENDING_PAYMENT)
                .orderTime(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .entries(new ArrayList<>())
                .build();

        // Elaboro le singole righe dell'ordine (Entries)
        for (OrderEntryRequestDTO entryDto : orderRequestDto.items()) {

            // Recupero il prodotto e verifico l'esistenza
            ProductEntity product = this.productRepository.findById(entryDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto con ID: " + entryDto.productId() + " non trovato"));

            // Verifico la disponibilità in magazzino
            if (product.getStock() < entryDto.quantity())
                throw new OutOfStockException("Disponibilità insufficiente per il prodotto: " + product.getName() +
                        " (Richiesti: " + entryDto.quantity() + ", Disponibili: " + product.getStock() + ")");

            // Scalo la quantità dallo stock del prodotto
            product.setStock(product.getStock() - entryDto.quantity());
            this.productRepository.save(product);

            // Creo la riga di dettaglio dell'ordine (congelando prezzo e nome attuale)
            OrderEntryEntity newOrderEntry = OrderEntryEntity.builder()
                    .product(product)
                    .productName(product.getName())
                    .quantity(entryDto.quantity())
                    .price(product.getPrice())
                    .total(product.getPrice().multiply(BigDecimal.valueOf(entryDto.quantity())))
                    .build();

            // Aggiorno il totale dell'ordine e aggiungo la riga
            newOrder.setTotal(newOrder.getTotal().add(newOrderEntry.getTotal()));
            newOrder.addEntry(newOrderEntry);
        }

        // Salvo l'ordine inizialmente
        OrderEntity savedOrder = this.orderRepository.save(newOrder);

        // Genero la sessione Stripe
        try {
            Session session = stripeService.createCheckoutSession(savedOrder);
            savedOrder.setStripeSessionId(session.getId());
            this.orderRepository.save(savedOrder);

            OrderResponseDTO dto = this.orderMapper.toDto(savedOrder);
            // Poiché OrderResponseDTO è un record, dobbiamo ricostruirlo per aggiungere l'URL
            return new OrderResponseDTO(
                    dto.id(), dto.user(), dto.status(), dto.orderTime(),
                    dto.entries(), dto.total(), session.getId(), session.getUrl()
            );
        } catch (StripeException e) {
            throw new RuntimeException("Errore durante la creazione della sessione di pagamento Stripe", e);
        }
    }

    /**
     * Finalizza l'acquisto convertendo il carrello attuale dell'utente in un ordine definitivo.
     */
    @Override
    @Transactional
    public OrderResponseDTO checkout() {

        // Recupero l'utente attualmente loggato tramite SecurityUtils
        UserEntity currentUser = securityUtils.getCurrentUser();

        // Validazione dati di spedizione
        validateUserAddress(currentUser);

        // Recupero il carrello dell'utente
        CartEntity cart = this.cartRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrello non trovato per l'utente corrente"));

        // Verifico che il carrello non sia vuoto
        if (cart.getItems().isEmpty()) {
            throw new InvalidOrderStateException("Impossibile procedere al checkout: il carrello è vuoto.");
        }

        // Inizializzo l'entità dell'ordine
        OrderEntity newOrder = OrderEntity.builder()
                .user(currentUser)
                .status(OrderStatus.PENDING_PAYMENT)
                .orderTime(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .entries(new ArrayList<>())
                .build();

        // Trasformo ogni elemento del carrello in una riga d'ordine
        for (CartItemEntity cartItem : cart.getItems()) {
            ProductEntity product = cartItem.getProduct();

            // Verifico disponibilità stock
            if (product.getStock() < cartItem.getQuantity())
                throw new OutOfStockException("Disponibilità insufficiente per il prodotto: " + product.getName());

            // Aggiorno magazzino
            product.setStock(product.getStock() - cartItem.getQuantity());
            this.productRepository.save(product);

            // Creo riga d'ordine (OrderEntry)
            OrderEntryEntity orderEntry = OrderEntryEntity.builder()
                    .product(product)
                    .productName(product.getName())
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .total(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build();

            // Aggiorno totale ordine
            newOrder.setTotal(newOrder.getTotal().add(orderEntry.getTotal()));
            newOrder.addEntry(orderEntry);
        }

        // Salvo l'ordine e svuoto il carrello
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        cart.getItems().clear();
        this.cartRepository.save(cart);

        // Genero la sessione Stripe
        try {
            Session session = stripeService.createCheckoutSession(savedOrder);
            savedOrder.setStripeSessionId(session.getId());
            this.orderRepository.save(savedOrder);

            OrderResponseDTO dto = this.orderMapper.toDto(savedOrder);
            return new OrderResponseDTO(
                    dto.id(), dto.user(), dto.status(), dto.orderTime(),
                    dto.entries(), dto.total(), session.getId(), session.getUrl()
            );
        } catch (StripeException e) {
            throw new RuntimeException("Errore durante la creazione della sessione di pagamento Stripe", e);
        }
    }

    /**
     * Recupera lo storico di tutti gli ordini effettuati dall'utente autenticato (Paginato).
     */
    @Override
    public Page<OrderResponseDTO> getMyOrders(Pageable pageable) {

        UserEntity currentUser = securityUtils.getCurrentUser();
        Page<OrderEntity> ordersPage = this.orderRepository.findByUser_Id(currentUser.getId(), pageable);
        
        return ordersPage.map(this.orderMapper::toDto);
    }

    /**
     * Annulla un ordine e imposta lo stato a CANCELED.
     * Consente l'operazione solo se l'ordine appartiene all'utente e non è ancora stato spedito.
     */
    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId) {

        UserEntity currentUser = securityUtils.getCurrentUser();
        
        // Cerco l'ordine e verifico l'esistenza e la proprietà
        OrderEntity order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato"));

        if (!order.getUser().getId().equals(currentUser.getId()))
            throw new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato per questo utente.");

        // Verifico lo stato attuale: non deve essere già annullato
        if (order.getStatus() == OrderStatus.CANCELED)
            throw new InvalidOrderStateException("L'ordine è già stato annullato.");

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)
            throw new InvalidOrderStateException("Impossibile annullare l'ordine: è già stato spedito o consegnato.");

        // Ripristino stock: per ogni voce dell'ordine, restituisci la quantità al magazzino
        for (OrderEntryEntity entry : order.getEntries()) {
            ProductEntity product = entry.getProduct();
            product.setStock(product.getStock() + entry.getQuantity());
            this.productRepository.save(product);
        }

        // Aggiorno lo stato in CANCELED
        order.setStatus(OrderStatus.CANCELED);
        this.orderRepository.save(order);
        
        return this.orderMapper.toDto(order);
    }



    // AREA ADMIN

    /**
     * Modifica lo stato di un ordine specifico.
     */
    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus newStatus) {

        OrderEntity order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato"));

        order.setStatus(newStatus);
        this.orderRepository.save(order);
        
        return this.orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(String sessionId) {
        OrderEntity order = this.orderRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordine con Stripe Session ID: " + sessionId + " non trovato"));

        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.PAID);
            this.orderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public void handlePaymentFailure(String sessionId) {
        OrderEntity order = this.orderRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordine con Stripe Session ID: " + sessionId + " non trovato"));

        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            
            // Ripristino stock
            for (OrderEntryEntity entry : order.getEntries()) {
                ProductEntity product = entry.getProduct();
                product.setStock(product.getStock() + entry.getQuantity());
                this.productRepository.save(product);
            }
            
            this.orderRepository.save(order);
        }
    }

    /**
     * Verifica che l'utente abbia inserito i dati minimi di spedizione nel profilo.
     */
    private void validateUserAddress(UserEntity user) {
        if (user.getAddress() == null || user.getAddress().isBlank() ||
            user.getCity() == null || user.getCity().isBlank() ||
            user.getCountry() == null || user.getCountry().isBlank()) {
            throw new InvalidOrderStateException("Profilo incompleto: inserisci indirizzo, città e nazione nel tuo profilo per procedere con l'ordine.");
        }
    }
}
