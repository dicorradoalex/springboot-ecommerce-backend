package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.config.JwtService;
import com.lipari.Academy2026.dto.AuthResponseDTO;
import com.lipari.Academy2026.dto.LoginRequestDTO;
import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.enums.UserRole;
import com.lipari.Academy2026.exception.AlreadyExistsException;
import com.lipari.Academy2026.mapper.UserMapper;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Validazione credenziali
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Recupero utente dal DB
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        // Generazione JWT
        String jwtToken = jwtService.generateToken(user);

        // Risposta
        return new AuthResponseDTO(jwtToken, "Bearer");
    }

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationRequestDTO registrationDTO) {
        // Controllo se esiste già un utente con la stessa email
        if (this.userRepository.existsByEmail(registrationDTO.email()))
            throw new AlreadyExistsException("Email già registrata: " + registrationDTO.email());

        // DTO -> Entity
        UserEntity user = this.userMapper.toEntity(registrationDTO);

        // Hashing della password per sicurezza
        String encodedPassword = this.passwordEncoder.encode(registrationDTO.password());
        user.setPassword(encodedPassword);

        // Assegnazione ruolo di default (USER)
        user.setRole(UserRole.USER);

        // Salvo e restituisco DTO
        user = this.userRepository.save(user);
        return this.userMapper.toDto(user);
    }
}