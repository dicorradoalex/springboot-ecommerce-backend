package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Endpoint pubblico (vedi SecurityConfig)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRegistrationRequestDTO registrationDTO) {

        // Registrazione utente
        UserResponseDTO response = this.userService.registerUser(registrationDTO);

        // Restituiamo 201 Created + utente (senza password)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}