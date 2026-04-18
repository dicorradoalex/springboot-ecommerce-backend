package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.dto.UserUpdateRequestDTO;
import com.lipari.Academy2026.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Da richiesta di registrazione a Entity (per salvare nel DB)
    UserEntity toEntity(UserRegistrationRequestDTO registrationDTO);

    // Da Entity a risposta (per inviare al client senza password)
    UserResponseDTO toDto(UserEntity entity);

    // Da Lista di entity a lista di DTO
    List<UserResponseDTO> toDtoList(List<UserEntity> entities);

    // Aggiornamento UserEntity da DTO di richiesta
    void updateEntityFromUpdateDto(UserUpdateRequestDTO dto, @MappingTarget UserEntity entity);
}
