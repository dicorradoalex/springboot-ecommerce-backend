package com.lipari.Academy2026.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO per la richiesta di login.
 */
public record LoginRequestDTO(
        
        @Email(message = "Il formato dell'email non è valido")
        @NotBlank(message = "L'email è obbligatoria")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        String password
) {}