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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // DIPENDENZE
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Esegue l'autenticazione dell'utente tramite email e password e genera un token JWT.
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {

        // Eseguo l'autenticazione tramite Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Recupero i dati dell'utente dal database
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        // Genero il token JWT per la sessione stateless
        String jwtToken = jwtService.generateToken(user);

        // Restituisco la risposta completa con token, email e ruolo
        return new AuthResponseDTO(
                jwtToken,
                user.getEmail(),
                user.getRole().name()
        );
    }

    /**
     * Registra un nuovo account utente nel sistema applicando l'hashing alla password.
     */
    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationRequestDTO registrationDTO) {

        // Verifico l'univocità dell'indirizzo email
        if (this.userRepository.existsByEmail(registrationDTO.email()))
            throw new AlreadyExistsException("Email già registrata: " + registrationDTO.email());

        // Converto il DTO in Entità tramite il mapper
        UserEntity user = this.userMapper.toEntity(registrationDTO);

        // Codifico la password per garantire la sicurezza nel database
        String encodedPassword = this.passwordEncoder.encode(registrationDTO.password());
        user.setPassword(encodedPassword);

        // Assegno il ruolo predefinito (USER) per i nuovi iscritti
        user.setRole(UserRole.USER);

        // Persisto l'utente e restituisco il DTO di risposta
        UserEntity savedUser = this.userRepository.save(user);
        return this.userMapper.toDto(savedUser);
    }
}
