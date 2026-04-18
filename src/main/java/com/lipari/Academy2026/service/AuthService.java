package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.AuthResponseDTO;
import com.lipari.Academy2026.dto.LoginRequestDTO;
import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;

public interface AuthService {

    // Esegue il login e restituisce il token JWT
    AuthResponseDTO login(LoginRequestDTO request);

    // Esegue la registrazione di un nuovo utente
    UserResponseDTO registerUser(UserRegistrationRequestDTO registrationDTO);
}