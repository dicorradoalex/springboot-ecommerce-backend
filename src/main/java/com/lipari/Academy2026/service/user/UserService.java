package com.lipari.Academy2026.service.user;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaccia per la gestione degli utenti e dei profili.
 */
public interface UserService {

    UserResponseDTO getCurrentUser();
    UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO);
    UserResponseDTO getUser(UUID id);
    void deleteUser(UUID id);
    Page<UserResponseDTO> getAllUsers(Pageable pageable);
}
