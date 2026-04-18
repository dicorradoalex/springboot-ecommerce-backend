package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.AuthResponseDTO;
import com.lipari.Academy2026.dto.LoginRequestDTO;
import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Dipendenze
    private final AuthService authService;

    /**
     * Endpoint per la registrazione di un nuovo utente.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRegistrationRequestDTO registrationDTO) {

        UserResponseDTO response = this.authService.registerUser(registrationDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Endpoint per il login (scambio credenziali -> token).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginDTO) {

        AuthResponseDTO response = this.authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }
}
