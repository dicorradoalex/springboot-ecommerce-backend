package com.lipari.Academy2026.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per la richiesta di registrazione di un nuovo utente.
 * Include i dati anagrafici e le credenziali di accesso.
 */
public record UserRegistrationRequestDTO(

        @NotBlank(message = "Il nome è obbligatorio")
        String name,

        @NotBlank(message = "Il cognome è obbligatorio")
        String surname,

        @Email(message = "Il formato dell'email non è valido")
        @NotBlank(message = "L'email è obbligatoria")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
        String password,

        String address,

        String city,

        String country
) {}