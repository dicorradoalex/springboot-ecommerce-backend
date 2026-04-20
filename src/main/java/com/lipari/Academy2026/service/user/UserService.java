package com.lipari.Academy2026.service.user;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

/**
 * Interfaccia per la gestione degli utenti e dei profili.
 */
public interface UserService {

    UserResponseDTO getCurrentUser();
    UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO);
    UserResponseDTO getUser(UUID id);
    void deleteUser(UUID id);
    List<UserResponseDTO> getAllUsers();
}
