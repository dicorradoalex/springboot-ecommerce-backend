package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;
import com.lipari.Academy2026.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

/**
 * Gestisce la visualizzazione e la modifica del profilo utente.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // Dipendenze
    private final UserService userService;

    /**
     * Recupera il profilo dell'utente attualmente loggato.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {

        UserResponseDTO currentUser = this.userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Aggiorna i dati del profilo dell'utente loggato.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserUpdateRequestDTO updateDTO) {

        UserResponseDTO updatedUser = this.userService.updateUser(updateDTO);
        return ResponseEntity.ok(updatedUser);
    }


    // AREA ADMIN

    /**
     * Recupera la lista di tutti gli utenti registrati.
     */
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @PageableDefault(page = 0, size = 10, sort = "surname") Pageable pageable) {
        Page<UserResponseDTO> usersPage = this.userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

    /**
     * Recupera i dettagli di un utente specifico tramite il suo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserResponseDTO foundUser = this.userService.getUser(id);
        return ResponseEntity.ok(foundUser);
    }

    /**
     * Elimina un utente dal sistema tramite ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
