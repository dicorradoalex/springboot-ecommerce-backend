package com.lipari.Academy2026.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    Annotazioni LOMBOK

    - @Builder
      Permette la creazione fluida dell'oggetto. Obbligatorio @AllArgsConstructor.

    - @EqualsAndHashCode.Include
      Limita equals/hashCode solo ai campi scelti.
      Richiede (onlyExplicitlyIncluded = true) sulla classe.

    - @ToString.Exclude
      Evita il loop infinito (StackOverflow) con la lista di prodotti associati.

    Annotazioni JPA (Mappatura Database)

    - @OneToMany (INVERSE SIDE)
      Definisce il lato "riflesso" della relazione. Una categoria ha molti prodotti.

    - mappedBy = "category"
      Punta al nome della variabile 'category' presente nella classe ProductEntity (Owner Side).
      Indispensabile per non creare tabelle di giunzione inutili nel database.

    - @Column(nullable = false, length = 100)
      Garantisce che il nome della categoria sia obbligatorio e non superi i 100 caratteri.

-----------
    SINTESI
    - Lato Relazione: Inverse Side (non possiede la FK).
    - Legame: Mappato sul campo 'category' dell'altra entità.
    - Caricamento: Default LAZY (corretto per le liste).
*/