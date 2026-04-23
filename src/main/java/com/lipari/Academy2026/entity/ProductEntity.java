package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Rappresenta un prodotto fisico in vendita all'interno del catalogo dell'ecommerce.
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;


    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;


    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private CategoryEntity category;

    @Column(nullable = false)
    private boolean deleted = false;
}

/*
    NOTE DIDATTICHE

    Relazione ManyToOne:
       - Owner Side: Questa entità possiede la chiave esterna (category_id).

-----------
*/
