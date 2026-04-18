package com.lipari.Academy2026.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        String name,
        String surname,
        String address,
        String city,
        String country
) {}