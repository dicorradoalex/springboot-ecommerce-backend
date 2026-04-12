package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_entry")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private OrderEntity order;
}

/*
    NOTE DIDATTICHE

    Annotazioni LOMBOK
    - @Builder
      Permette di creare le righe d'ordine al volo durante il checkout.

    - @ToString.Exclude
      Evita loop e stackoverflow.

    Annotazioni JPA (Mappatura Database)
    - @ManyToOne (OWNER SIDE x2)
      Questa entità è proprietaria di DUE chiavi esterne: 'product_id' e 'order_id'.

    - Fetch Strategy (Best Practice)
      Usiamo FetchType.LAZY su entrambi i @ManyToOne per evitare di caricare
      roba inutile

    - Salvare il prezzo
      Salviamo il 'price' qui anche se esiste già in ProductEntity in
      modo che anche se il prezzo del prodotto cambierà, l'ordine del
      cliente deve mantenere il prezzo del momento dell'acquisto.

-----------
*/