package com.lipari.Academy2026.service;

import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.dto.UserUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UserResponseDTO getCurrentUser();

    UserResponseDTO getUser(UUID id);

    void deleteUser(UUID id);

    UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO registerUser(UserRegistrationRequestDTO registrationDTO);

}
