package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Lombok genera equals e hashCode usando solo i campi indicati esplicitamente
@ToString
@NoArgsConstructor // Genera un costruttore vuoto, serve per JPA per istanziare l'entità quando recupera i dati dal database
@AllArgsConstructor // Genera un costruttore con tutti i parametri
@Builder // Per costrutire un oggetto tipo: CategoryEntity.builder().name("Elettronica").build();
public class CategoryEntity {

    @Id // indica la chiave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Genera ID univico
    @EqualsAndHashCode.Include // Genera Equals e HashCode basandosi su questo campo
    private UUID id;

    private String name;
}
