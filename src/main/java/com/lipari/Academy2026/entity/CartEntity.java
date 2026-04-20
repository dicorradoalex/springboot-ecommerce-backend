package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Rappresenta il carrello della spesa associato a un utente.
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CartEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private UserEntity user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();

    /**
     * Metodo helper per gestire correttamente l'aggiunta di un elemento (relazione bidirezionale).
     */
    public void addItem(CartItemEntity item) {
        items.add(item);
        item.setCart(this);
    }

    /**
     * Metodo helper per gestire correttamente la rimozione di un elemento.
     */
    public void removeItem(CartItemEntity item) {
        items.remove(item);
        item.setCart(null);
    }
}

/*
    NOTE DIDATTICHE

    Relazioni OneToOne e OneToMany:
       - User (OneToOne): Un utente ha un solo carrello (unique = true).
       - Items (OneToMany): Il carrello gestisce il ciclo di vita dei suoi elementi (CascadeType.ALL, orphanRemoval = true).

    Metodi Helper:
       Per assicurarsi che entrambi i lati del legame siano aggiornati e per fare meno passaggi.
-----------
*/
