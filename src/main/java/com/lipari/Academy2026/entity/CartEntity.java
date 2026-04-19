package com.lipari.Academy2026.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // Helper per relazione bidirezionale
    public void addItem(CartItemEntity item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItemEntity item) {
        items.remove(item);
        item.setCart(null);
    }
}


/*
    NOTE DIDATTICHE - [CartEntity]

     @OneToOne (User):
       - Un utente ha un solo carrello
       - unique = true -> vincolo DB
       - fetch = LAZY -> performance migliore

     @OneToMany (Items):
       - Un carrello ha più elementi
       - mappedBy = "cart" -> relazione gestita da CartItemEntity
       - orphanRemoval = true -> elimina dal DB gli oggetti rimossi

*/