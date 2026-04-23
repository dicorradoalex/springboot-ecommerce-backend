package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Rappresenta una riga di dettaglio di un ordine, "congelando" prezzo e quantità al momento dell'acquisto.
 */
@Entity
@Table(name = "order_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class OrderEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private ProductEntity product;

    /**
     * Nome del prodotto al momento dell'acquisto.
     * Serve per garantire che lo storico ordini rimanga consultabile
     * anche se il prodotto originale viene rinominato o rimosso (soft-delete).
     */
    @Column(name = "product_name", nullable = true)
    private String productName;


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
    NOTE DIDATTICHE - [OrderEntryEntity]

    Immutabilità del Prezzo:
       A differenza del carrello, qui si salva esplicitamente il prezzo. Questo garantisce la validità dello storico
       ordini anche se il prezzo del prodotto nel catalogo dovesse cambiare in futuro.

    Relazioni:
       L'entità è proprietaria di due chiavi esterne (product_id e order_id), fa da congiunzione tra l'ordine e
       i prodotti venduti.
-----------
*/
