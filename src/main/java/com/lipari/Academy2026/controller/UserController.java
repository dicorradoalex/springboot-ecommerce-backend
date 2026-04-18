package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.dto.UserUpdateRequestDTO;
import com.lipari.Academy2026.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // Dipendenze
    private final UserService userService;

    /**
     * Recupera il profilo dell'utente attualmente loggato.
     * SICUREZZA: Non ha argomenti perché recupera l'ID utente dal token.
     * Utile per visualizzare il proprio profilo.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        // Chiedo al Service di recuperare i dati dell'utente autenticato
        UserResponseDTO currentUser = this.userService.getCurrentUser();
        
        // Restituisco 200 OK con i dati del profilo
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Aggiorna i dati del profilo dell'utente loggato.
     * SICUREZZA: Utilizzo un DTO specifico (UserUpdateRequestDTO) che non contiene l'ID.
     * L'identità dell'utente viene recuperata dal Token JWT nel Service.
     */
    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO updateDTO) {
        // Chiedo al Service di eseguire l'aggiornamento sicuro
        UserResponseDTO updatedUser = this.userService.updateUser(updateDTO);
        
        // Restituisco 200 OK con i dati aggiornati
        return ResponseEntity.ok(updatedUser);
    }

}
