package com.lipari.Academy2026.entity;

import com.lipari.Academy2026.enums.UserRole;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String surname;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(length = 200)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 50)
    private String country;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderEntity> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trasformiamo il ruolo in un'autorità Spring (es. ROLE_USER, ROLE_ADMIN)
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        // Usiamo l'email come username
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}


/*
    NOTE DIDATTICHE

    Scurity

    - UserDetails (I)
      Indica a Spring come leggere l'email, la password e il ruolo dell'utente dal db

    Annotazioni LOMBOK

    - @Builder
      Permette la creazione fluida dell'oggetto. Obbligatorio @AllArgsConstructor.

    - @EqualsAndHashCode.Include
      Limita equals/hashCode solo ai campi scelti.
      Richiede (onlyExplicitlyIncluded = true) sulla classe.

    - @ToString.Exclude
      Evita il loop infinito (StackOverflow) con la lista degli ordini associati.

    Annotazioni JPA (Mappatura Database)

    - @Table(name = "users")
      Specifica il nome della tabella. Si usa "users" perché "user" è una parola
      riservata in SQL.

    - @Column(unique = true)
      Garantisce che non esistano due utenti con la stessa email nel database.

    - @Column(length = ...)
      Definisce la dimensione massima della colonna VARCHAR.

    - @OneToMany (INVERSE SIDE)
      Rappresenta il lato inverso della relazione. Un utente può avere molti ordini.
      'mappedBy = "user"' indica che la FK è gestita dal campo 'user' in OrderEntity.

    - Fetch Strategy (Default):
      Le liste (@OneToMany) sono LAZY di default: i dati vengono scaricati
      solo alla chiamata del getter (es. user.getOrders()).
      Nota: @ManyToOne è invece EAGER di default.

-----------
    SINTESI
    - Lato Relazione: Inverse Side.
    - Vincoli: Email univoca e campi obbligatori (Name, Surname).
    - Tabella: Nominata "users" per sicurezza SQL.
*/
