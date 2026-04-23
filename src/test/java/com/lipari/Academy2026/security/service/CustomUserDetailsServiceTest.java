package com.lipari.Academy2026.security.service;

import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per CustomUserDetailsService.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("Caricamento utente: deve restituire UserDetails se l'email esiste")
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // GIVEN
        String email = "test@test.it";
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("encoded_pass")
                .build();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // WHEN
        UserDetails result = userDetailsService.loadUserByUsername(email);

        // THEN
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Caricamento utente: deve lanciare eccezione se l'email non esiste")
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        // GIVEN
        String email = "nonexistent@test.it";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }
}
