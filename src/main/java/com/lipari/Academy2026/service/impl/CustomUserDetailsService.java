package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
    È il componente che sa come recuperare gli utenti nel database quando qualcuno
    prova a loggarsi.
    Implementa UserDetailsService.
 */


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Recupera l'utente nel database
        Optional<UserEntity> userOptional = this.userRepository.findByEmail(email);
        if (!userOptional.isPresent())
            throw new UsernameNotFoundException("Utente non trovato con email: " + email);

        // Restituiamo l'entità (UserEntity implementa UserDetails)
        return userOptional.get();
    }
}