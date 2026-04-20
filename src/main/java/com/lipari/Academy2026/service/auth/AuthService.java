package com.lipari.Academy2026.service.auth;

import com.lipari.Academy2026.dto.auth.AuthResponseDTO;
import com.lipari.Academy2026.dto.auth.LoginRequestDTO;
import com.lipari.Academy2026.dto.auth.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.user.UserResponseDTO;

/**
 * Interfaccia per i servizi di autenticazione e registrazione utenti.
 */
public interface AuthService {

    AuthResponseDTO login(LoginRequestDTO request);
    UserResponseDTO registerUser(UserRegistrationRequestDTO registrationDTO);
}
