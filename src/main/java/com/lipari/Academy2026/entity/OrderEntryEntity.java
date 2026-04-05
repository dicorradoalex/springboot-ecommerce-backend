package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_entry")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntryEntity {

    @Id // indica la chiave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Genera ID univoco
    @EqualsAndHashCode.Include // Genera Equals e HashCode basandosi su questo campo
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude // Evita loop infiniti con il toString di user
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude // Evita loop infiniti con il toString di user
    private OrderEntity orderEntity;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

}
