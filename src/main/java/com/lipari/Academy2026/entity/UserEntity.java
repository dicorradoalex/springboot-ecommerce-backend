package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

import java.util.List;

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

    private String name;
    private String surname;
    private String address;
    private String city;
    private String country;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;


}
