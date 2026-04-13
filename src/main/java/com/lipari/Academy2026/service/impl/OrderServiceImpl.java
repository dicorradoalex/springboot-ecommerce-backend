package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.OrderDTO;
import com.lipari.Academy2026.dto.OrderEntryRequestDTO;
import com.lipari.Academy2026.dto.OrderRequestDTO;
import com.lipari.Academy2026.entity.OrderEntity;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.entity.UserEntity;
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
import java.util.Optional;

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
    public OrderDTO createOrder(OrderRequestDTO orderRequestDto) {

        // Verifica se l'utente "richiesto" esiste nel db
        Optional<UserEntity> user = this.userRepository.findById(orderRequestDto.userId());
        if (!user.isPresent())
            throw new ResourceNotFoundException("L'utente con ID: " + orderRequestDto.userId() + "non esiste nel db");

        // Inizializza l'ordine "richiesto"
        OrderEntity newOrder = OrderEntity.builder()
                .user(user.get())
                .status("CREATED") // Da rivedere (fare un enum)
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
                    .order(newOrder) // la collego all'ordine creato
                    .build();

            newOrder.setTotal(newOrder.getTotal().add(newOrderEntry.getTotal()));

            // L'aggiungo alla lista del nuovo ordine
            newOrder.getEntries().add(newOrderEntry);
        }

        // Salvo
        this.orderRepository.save(newOrder);
        OrderDTO orderCreated = this.orderMapper.toDto(newOrder);
        return orderCreated;
    }
}

