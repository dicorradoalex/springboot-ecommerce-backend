package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.auth.UserRegistrationRequestDTO;
import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;
import com.lipari.Academy2026.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte il DTO di registrazione in una nuova entità utente per il salvataggio.
     */
    UserEntity toEntity(UserRegistrationRequestDTO registrationDTO);

    /**
     * Trasforma l'entità utente in un DTO di risposta sicuro (senza password).
     */
    UserResponseDTO toDto(UserEntity entity);

    /**
     * Trasforma una lista di entità utente in una lista di DTO di risposta.
     */
    List<UserResponseDTO> toDtoList(List<UserEntity> entities);

    /**
     * Aggiorna un'entità utente esistente con i dati provenienti dal DTO di aggiornamento.
     */
    void updateEntityFromUpdateDto(UserUpdateRequestDTO dto, @MappingTarget UserEntity entity);
}
