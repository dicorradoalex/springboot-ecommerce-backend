package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductEntity {

    @Id
    @EqualsAndHashCode.Include // Genera Equals e HashCode basandosi su questo campo (id)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private BigDecimal price;

    private String description;

    // Molti prodotti possono avere la stessa categoria (serve a JPA)
    @ManyToOne()
    @JoinColumn(name = "category_id")
    @ToString.Exclude // Evita loop infiniti con il toString della categoria
    private CategoryEntity category;
}
