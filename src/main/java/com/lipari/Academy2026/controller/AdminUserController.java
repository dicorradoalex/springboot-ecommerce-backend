package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * AREA ADMIN: Gestisce tutti gli utenti del sistema
 */

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * Recupera la lista di tutti gli utenti registrati.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> usersList = this.userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    /**
     * Recupera i dettagli di un utente specifico tramite il suo ID.
     * Utile per visualizzare un profilo specifico o per gestione.
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
