package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Rappresenta un singolo elemento (prodotto e quantità) all'interno di un carrello.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @ToString.Exclude
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;
}

/*
    NOTE DIDATTICHE - [CartItemEntity]

    Relazioni ManyToOne:
       Più righe possono appartenere allo stesso carrello e puntare a prodotti diversi.
-----------
*/
