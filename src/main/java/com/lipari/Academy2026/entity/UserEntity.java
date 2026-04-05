package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Equals e HashCode su campi indicati
@NoArgsConstructor // Costruttore senza parametri per JPA quando instanzia l'entità per recuperare i dati dal db
@AllArgsConstructor // Costruttore canonico
@Builder
public class UserEntity {

    @Id // indica la chiave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Genera ID univoco
    @EqualsAndHashCode.Include // Genera Equals e HashCode basandosi su questo campo
    private UUID id;

    String name;
    String surname;
    String address;
    String city;
    String country;

}
