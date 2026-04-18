package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.dto.UserUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // Recupera il profilo dell'utente loggato (User)
    UserResponseDTO getCurrentUser();

    // Aggiorna il profilo dell'utente loggato (User)
    UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO);

    // Recupera un utente per ID (Admin)
    UserResponseDTO getUser(UUID id);

    // Elimina un utente (Admin)
    void deleteUser(UUID id);

    // Elenca tutti gli utenti (Admin)
    List<UserResponseDTO> getAllUsers();
}
