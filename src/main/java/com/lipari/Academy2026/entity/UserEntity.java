package com.lipari.Academy2026.entity;

import com.lipari.Academy2026.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Rappresenta l'utente del sistema, integrando le specifiche di sicurezza di Spring Security.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Cognome dell'utente.
     */
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private CartEntity cart;

    // Per soft delete
    @Column(nullable = false)
    private boolean deleted = false;

    // METODI SPRING SECURITY (UserDetails)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
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
        return !this.deleted;
    }
}

/*
    NOTE DIDATTICHE

    Security (UserDetails):
       L'implementazione dell'interfaccia UserDetails permette a Spring Security di utilizzare
       questa entità per l'autenticazione

    Relazioni e Cascade:
       Si usa CascadeType.ALL per ordini e carrello perché se elimino un utente voglio cancellare automaticamente
       anche tutti i suoi ordini
-----------
*/
