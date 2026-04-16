package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.dto.OrderEntryRequestDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;
import com.lipari.Academy2026.entity.OrderEntity;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.enums.OrderStatus;
import com.lipari.Academy2026.exception.InvalidOrderStateException;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.OrderMapper;
import com.lipari.Academy2026.repository.OrderRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.service.OrderService;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto) {

        // Verifica se l'utente "richiesto" esiste nel db
        Optional<UserEntity> user = this.userRepository.findById(orderRequestDto.userId());
        if (!user.isPresent())
            throw new ResourceNotFoundException("L'utente con ID: " + orderRequestDto.userId() + "non esiste nel db");

        // Inizializza l'ordine "richiesto"
        OrderEntity newOrder = OrderEntity.builder()
                .user(user.get())
                .status(OrderStatus.CREATED)
                .orderTime(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .entries(new ArrayList<>())
                .build();

        // Crea le Entry "richieste"
        for (OrderEntryRequestDTO entry : orderRequestDto.entries()) {
            // Estraggo il product "richiesto" dal db
            Optional<ProductEntity> product = this.productRepository.findById(entry.productId());
            if (!product.isPresent())
                throw new ResourceNotFoundException("Prodotto con ID: " + entry.productId() + " non trovato");

            // Crea una nuova entry prendendo i dati dal dto (usa il builder)
            OrderEntryEntity newOrderEntry = OrderEntryEntity.builder()
                    .product(product.get())
                    .quantity(entry.quantity())
                    .price(product.get().getPrice())
                    .total(product.get().getPrice().multiply(BigDecimal.valueOf(entry.quantity())))
                    .build();

            newOrder.setTotal(newOrder.getTotal().add(newOrderEntry.getTotal()));

            // L'aggiungo alla lista del nuovo ordine
            newOrder.addEntry(newOrderEntry);
        }

        // Salvo
        this.orderRepository.save(newOrder);
        OrderResponseDTO orderCreated = this.orderMapper.toDto(newOrder);
        return orderCreated;
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
    public List<OrderResponseDTO> getOrdersByUser(UUID userId) {
        // Verifico se l'utente esiste
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);

        if (!userOptional.isPresent())
            throw new ResourceNotFoundException("Utente con ID: " + userId + " non trovato");

        // Recupero la lista di entità dal repository
        List<OrderEntity> ordersList = this.orderRepository.findByUser_Id(userId);

        // Converto la lista di entità in lista di DTO e restituisco
        return this.orderMapper.toDtoList(ordersList);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId) {
        // Cerco l'ordine nel DB
        Optional<OrderEntity> orderOptional = this.orderRepository.findById(orderId);

        // Controllo se esiste
        if (!orderOptional.isPresent()) {
            throw new ResourceNotFoundException("Ordine con ID: " + orderId + " non trovato");
        }

        OrderEntity order = orderOptional.get();

        // Controllo lo stato attuale:
        // Se l'ordine è già SPEDITO o CONSEGNATO, non si può annullare
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)
            throw new InvalidOrderStateException("Impossibile annullare l'ordine: è già stato spedito o consegnato.");

        // Se tutto ok, cambio lo stato in CANCELED
        order.setStatus(OrderStatus.CANCELED);

        // Salvo l'entità aggiornata
        this.orderRepository.save(order);

        // Converto in DTO e restituisco
        return this.orderMapper.toDto(order);
    }
}

