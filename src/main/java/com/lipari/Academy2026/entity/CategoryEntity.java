package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Rappresenta una categoria per raggruppare i prodotti.
 */
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;


    @Column(nullable = false, length = 100)
    private String name;


    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<ProductEntity> productsList;
}

/*
    NOTE DIDATTICHE

    Relazione OneToMany (Inverse Side):
       'mappedBy = "category"' indica che il legame è gestito dal campo 'category' in ProductEntity.

    Lombok:
       @ToString.Exclude sulla lista prodotti previene loop infiniti.
-----------
*/
