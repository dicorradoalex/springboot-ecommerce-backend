package com.lipari.Academy2026.entity;

import com.lipari.Academy2026.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderEntryEntity> entries;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

}

/*
    NOTE DIDATTICHE

    Annotazioni LOMBOK
    - @Table(name = "orders")
      Si usa il plurale perché "order" è una parola riservata in SQL (ORDER BY).

    - @ToString.Exclude
      Serve su 'user' e 'entries' per evitare StackOverflow nei log.

    Annotazioni JPA (Mappatura Database)
    - @ManyToOne (OWNER SIDE verso User)
      L'ordine "possiede" la chiave esterna 'user_id'. È il lato che proprietario del
      il legame con l'utente.

    - @OneToMany (INVERSE SIDE verso OrderEntry)
      L'ordine non ha colonne per le entries nel DB. Il legame è "mappato"
      dal campo 'order' presente nella classe OrderEntryEntity.

    - Fetch Strategy
      1. Verso User: @ManyToOne è EAGER di default (si consiglia LAZY).
      2. Verso Entries: @OneToMany è LAZY di default (ottimo per le performance).

    - LocalDateTime
      Mappa automaticamente la data e l'ora nel formato corretto del database
      (TIMESTAMP o DATETIME).

    - @Enumerated(EnumType.STRING)
      Con questa annotazione JPA salva il valore come una stringa e non come indice (cmportamento
      di default)
-----------

*/