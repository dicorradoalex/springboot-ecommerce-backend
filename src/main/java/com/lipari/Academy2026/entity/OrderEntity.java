package com.lipari.Academy2026.entity;

import com.lipari.Academy2026.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Rappresenta un ordine d'acquisto effettuato da un utente.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
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

    /**
     * ID della sessione di checkout di Stripe per riconciliare il pagamento via Webhook.
     */
    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    public void addEntry(OrderEntryEntity entry) {
        if (entry != null) {
            this.entries.add(entry);
            entry.setOrder(this);
        }
    }
}

/*
    NOTE DIDATTICHE - [OrderEntity]

    Naming:
       Usiamo @Table(name = "orders") perché "ORDER" è una parola riservata in SQL.

    Enum:
       @Enumerated(EnumType.STRING) salva lo stato come testo piuttosto che come indici.

    Ciclo di vita:
       Utilizziamo CascadeType.ALL per garantire che al salvataggio dell'ordine vengano salvate automaticamente anche
       tutte le sue entries.
-----------
*/
