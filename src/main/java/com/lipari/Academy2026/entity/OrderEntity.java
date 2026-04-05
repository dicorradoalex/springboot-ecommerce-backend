package com.lipari.Academy2026.entity;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "orders") // orders perché order è una parola riservata in sql
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Lombok genera equals e hashCode usando solo i campi indicati esplicitamente
@ToString
@NoArgsConstructor // Genera un costruttore vuoto, serve per JPA per istanziare l'entità quando recupera i dati dal database
@AllArgsConstructor // Genera un costruttore con tutti i parametri
@Builder // Per costrutire un oggetto tipo: CategoryEntity.builder().name("Elettronica").build();
public class OrderEntity {

    @Id // indica la chiave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Genera ID univico
    @EqualsAndHashCode.Include // Genera Equals e HashCode basandosi su questo campo
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude // Evita loop infiniti con il toString di user
    private UserEntity user;

    @Column(nullable = false)
    private String status;

    @Column(name = "order_time", nullable = false)
    LocalDateTime orderTime;

}