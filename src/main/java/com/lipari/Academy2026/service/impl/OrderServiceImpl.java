package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.dto.OrderEntryRequestDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;
import com.lipari.Academy2026.entity.CartEntity;
import com.lipari.Academy2026.entity.CartItemEntity;
import com.lipari.Academy2026.entity.OrderEntity;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.exception.InvalidOrderStateException;
import com.lipari.Academy2026.exception.OutOfStockException;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.OrderMapper;
import com.lipari.Academy2026.repository.CartRepository;
import com.lipari.Academy2026.repository.OrderRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    // Dipendenze
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto) {

        // Recupero utente loggato
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Inizializza l'ordine
        OrderEntity newOrder = OrderEntity.builder()
                .user(currentUser) // associa all'utente loggato
                .status(OrderStatus.CREATED)
                .orderTime(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .entries(new ArrayList<>())
                .build();

        // Elaborazione delle righe dell'ordine (Entries)
        for (OrderEntryRequestDTO entryDto : orderRequestDto.entries()) {

            // Recupero prodotto dal database
            Optional<ProductEntity> productOpt = this.productRepository.findById(entryDto.productId());

            // Controllo esistenza
            if (!productOpt.isPresent())
                throw new ResourceNotFoundException("Prodotto con ID: " + entryDto.productId() + " non trovato");


            ProductEntity product = productOpt.get();

            // Controllo disponibilità in magazzino
            if (product.getStock() < entryDto.quantity())
                throw new OutOfStockException("Disponibilità insufficiente per il prodotto: " + product.getName() +
                        " (Richiesti: " + entryDto.quantity() + ", Disponibili: " + product.getStock() + ")");


            // Aggiornamento Scorte:
            // Sottraggo la quantità ordinata dallo stock attuale
            product.setStock(product.getStock() - entryDto.quantity());
            this.productRepository.save(product);

            // Creazione entry "richiesta"
            OrderEntryEntity newOrderEntry = OrderEntryEntity.builder()
                    .product(product)
                    .quantity(entryDto.quantity())
                    .price(product.getPrice()) // Salva il prezzo attuale al momento dell'acquisto
                    .total(product.getPrice().multiply(BigDecimal.valueOf(entryDto.quantity())))
                    .build();

            // Aggiorno il totale dell'ordine e aggiungiamo la entry all'ordine
            newOrder.setTotal(newOrder.getTotal().add(newOrderEntry.getTotal()));
            newOrder.addEntry(newOrderEntry);
        }

        // Salvo e restituisco
        this.orderRepository.save(newOrder);
        return this.orderMapper.toDto(newOrder);
    }

    /**
     * Trasformo il carrello dell'utente in un ordine definitivo.
     */
    @Override
    @Transactional
    public OrderResponseDTO checkout() {
        // Recupero l'utente attualmente loggato nel sistema
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Cerco il carrello associato all'utente
        CartEntity cart = this.cartRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrello non trovato per l'utente corrente"));

        // Verifico che il carrello non sia vuoto prima di procedere
        if (cart.getItems().isEmpty()) {
            throw new InvalidOrderStateException("Impossibile procedere al checkout: il carrello è vuoto.");
        }

        // Inizializzo l'entità dell'ordine
        OrderEntity newOrder = OrderEntity.builder()
                .user(currentUser)
                .status(OrderStatus.CREATED)
                .orderTime(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .entries(new ArrayList<>())
                .build();

        // Ciclo su ogni elemento presente nel carrello per creare le righe d'ordine
        for (CartItemEntity cartItem : cart.getItems()) {
            ProductEntity product = cartItem.getProduct();

            // Verifico se il prodotto è disponibile in magazzino
            if (product.getStock() < cartItem.getQuantity())
                throw new OutOfStockException("Disponibilità insufficiente per il prodotto: " + product.getName());


            // Sottraggo la quantità acquistata dallo stock del prodotto e aggiorno
            product.setStock(product.getStock() - cartItem.getQuantity());
            this.productRepository.save(product);

            // Creo la riga d'ordine "congelando" il prezzo di vendita attuale
            OrderEntryEntity orderEntry = OrderEntryEntity.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .total(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build();

            // Aggiungo la riga all'ordine e aggiorno il totale complessivo
            newOrder.setTotal(newOrder.getTotal().add(orderEntry.getTotal()));
            newOrder.addEntry(orderEntry);
        }

        // Salvo l'ordine nel database
        OrderEntity savedOrder = this.orderRepository.save(newOrder);

        // Svuoto completamente il carrello dopo l'acquisto
        cart.getItems().clear();
        this.cartRepository.save(cart);

        // Converto in DTO e restituisco il risultato
        return this.orderMapper.toDto(savedOrder);
    }


    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        // Cerco l'ordine nel DB
        Optional<OrderEntity> orderOptional = this.orderRepository.findById(orderId);
        
        // Se non lo trovo lancio eccezione
        if (!orderOptional.isPresent())
            throw new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato");

        // Se lo trovo lo estraggo e aggiorno lo stato
        OrderEntity order = orderOptional.get();
        order.setStatus(newStatus);

        // Salvo l'entità aggiornata
        this.orderRepository.save(order);

        // Converto in DTO e restituisco
        return this.orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDTO> getMyOrders() {
        // Recupero l'utente loggato dal contesto di sicurezza
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Recupero la lista di entità dal repository usando l'ID dell'utente autenticato
        List<OrderEntity> ordersList = this.orderRepository.findByUser_Id(currentUser.getId());

        // Converto la lista di entità in lista di DTO e restituisco
        return this.orderMapper.toDtoList(ordersList);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId) {
        // Recupero utente loggato
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Cerco l'ordine nel DB
        Optional<OrderEntity> orderOptional = this.orderRepository.findById(orderId);

        // Controllo se l'ordine esiste
        if (!orderOptional.isPresent()) {
            throw new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato");
        }

        OrderEntity order = orderOptional.get();

        // Controllo proprietà
        // Verifica che l'ID dell'utente dell'ordine sia uguale all'ID dell'utente loggato
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato per questo utente.");
        }

        // Controllo lo stato attuale:
        // Se l'ordine è già SPEDITO o CONSEGNATO, non si può annullare
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("Impossibile annullare l'ordine: è già stato spedito o consegnato.");
        }

        // Se tutto ok, cambio lo stato in CANCELED
        order.setStatus(OrderStatus.CANCELED);

        // Salvo l'entità aggiornata
        this.orderRepository.save(order);

        // Converto in DTO e restituisco
        return this.orderMapper.toDto(order);
    }
}

