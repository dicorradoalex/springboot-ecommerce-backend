package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    Relazioni @ManyToOne:
       - Più elementi appartengono a un solo carrello (CartEntity)
       - Più elementi possono riferirsi allo stesso prodotto (ProductEntity)

    Prezzo:
       - Non viene salvato nel carrello
       - Si usa sempre il prezzo aggiornato del prodotto
       - product.getPrice() -> valore dinamico

    (Diverso dagli ordini, dove il prezzo va "congelato")
*/