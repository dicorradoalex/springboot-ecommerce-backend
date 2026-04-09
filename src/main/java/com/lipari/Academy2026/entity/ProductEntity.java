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
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private CategoryEntity category;
}


/*
    NOTE DIDATTICHE

    Annotazioni LOMBOK

    - @Builder
      Permette la creazione fluida dell'oggetto (es: .builder().name("..").build()).
      Obbligatorio abbinarlo a @AllArgsConstructor.

    - @EqualsAndHashCode.Include
      Limita equals/hashCode solo ai campi scelti.
      Richiede (onlyExplicitlyIncluded = true) sulla classe.

    - @ToString.Exclude
      Fondamentale nelle relazioni per evitare lo StackOverflow (loop infinito tra entità).

    Annotazioni JPA (Mappatura Database)

    - @Column(columnDefinition = "TEXT")
      Forza il tipo TEXT nel DB (>255 caratteri di VARCHAR).
      Si usa per descrizioni lunghe.

    - @ManyToOne (OWNER SIDE)
      Definisce che questa tabella contiene la chiave esterna (FK).

    - @JoinColumn(name = "nome_colonna")
      Specifica il nome fisico della colonna FK nel database.

    - FetchType.LAZY
      Caricamento ottimizzato. L'entità collegata è un "Proxy" (guscio vuoto)
      e i dati vengono scaricati solo se richiamati esplicitamente nel codice.


-----------
    SINTESI
    - Annotazioni JPA (Database): @ManyToOne, @JoinColumn, @Column, @Id, @GeneratedValue.
    - Annotazioni Lombok (Utility): @Builder, @ToString, @EqualsAndHashCode, @Getter/Setter.
*/
