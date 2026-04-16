package com.lipari.Academy2026.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;
import java.util.List;

public record UserResponseDTO(
        UUID id,
        String email,
        String name,
        String surname,
        String address,
        String city,
        String country
) {}