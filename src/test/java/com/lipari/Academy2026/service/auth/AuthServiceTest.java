package com.lipari.Academy2026.service.auth;

import com.lipari.Academy2026.dto.auth.AuthResponseDTO;
import com.lipari.Academy2026.dto.auth.LoginRequestDTO;
import com.lipari.Academy2026.dto.auth.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.enums.UserRole;
import com.lipari.Academy2026.exception.AlreadyExistsException;
import com.lipari.Academy2026.mapper.UserMapper;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per AuthServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Registrazione: deve codificare la password e salvare il nuovo utente")
    void registerUser_ShouldEncodePasswordAndSave() {
        // GIVEN
        UserRegistrationRequestDTO request = new UserRegistrationRequestDTO(
                "Mario", "Rossi", "mario@test.it", "password123", null, null, null
        );
        UserEntity userEntity = new UserEntity();
        UserEntity savedUser = new UserEntity();
        UserResponseDTO expectedDto = new UserResponseDTO(UUID.randomUUID(), "mario@test.it", "Mario", "Rossi", null, null, null);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(userEntity);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

        // WHEN
        UserResponseDTO result = authService.registerUser(request);

        // THEN
        assertNotNull(result);
        assertEquals("mario@test.it", result.email());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(userEntity);
        // Verifichiamo che i parametri critici siano stati settati nell'entità
        assertEquals("encodedPassword", userEntity.getPassword());
        assertEquals(UserRole.USER, userEntity.getRole());
    }

    @Test
    @DisplayName("Registrazione: deve lanciare eccezione se l'email esiste già")
    void registerUser_ShouldThrowException_WhenEmailExists() {
        // GIVEN
        UserRegistrationRequestDTO request = new UserRegistrationRequestDTO(
                "Mario", "Rossi", "mario@test.it", "password123", null, null, null
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // WHEN & THEN
        assertThrows(AlreadyExistsException.class, () -> authService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login: deve autenticare e generare il token JWT")
    void login_ShouldAuthenticateAndReturnToken() {
        // GIVEN
        LoginRequestDTO request = new LoginRequestDTO("mario@test.it", "password123");
        UserEntity user = UserEntity.builder()
                .email("mario@test.it")
                .role(UserRole.USER)
                .build();
        
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt.token.123");

        // WHEN
        AuthResponseDTO result = authService.login(request);

        // THEN
        assertNotNull(result);
        assertEquals("jwt.token.123", result.token());
        assertEquals("mario@test.it", result.email());
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(user);
    }
}
