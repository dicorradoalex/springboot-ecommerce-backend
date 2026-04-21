package com.lipari.Academy2026.service.user;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.UserMapper;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementazione del servizio per la gestione dei profili utente.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    // DIPENDENZE
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;

    /**
     * Recupera le informazioni del profilo dell'utente attualmente autenticato.
     */
    @Override
    public UserResponseDTO getCurrentUser() {

        UserEntity currentUser = securityUtils.getCurrentUser();

        return this.userMapper.toDto(currentUser);
    }

    /**
     * Aggiorna i dati anagrafici del profilo dell'utente loggato.
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO) {

        UserEntity currentUser = securityUtils.getCurrentUser();

        // Recupero l'utente dal database
        UserEntity userToUpdate = this.userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utente loggato non trovato nel database."));

        // Aggiorno i dati tramite il mapper e salvo le modifiche
        this.userMapper.updateEntityFromUpdateDto(updateDTO, userToUpdate);
        UserEntity savedUser = this.userRepository.save(userToUpdate);
        
        return this.userMapper.toDto(savedUser);
    }



    // AREA ADMIN

    /**
     * Recupera i dettagli di un utente specifico tramite ID.
     */
    @Override
    public UserResponseDTO getUser(UUID id) {

        // Cerco l'utente tramite ID
        UserEntity user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente con ID: " + id + " non trovato."));
        
        return this.userMapper.toDto(user);
    }

    /**
     * Rimuove definitivamente un utente dal sistema tramite ID.
     */
    @Override
    @Transactional
    public void deleteUser(UUID id) {

        if(!this.userRepository.existsById(id))
            throw new ResourceNotFoundException("Utente con ID: " + id + " non trovato.");
            
        this.userRepository.deleteById(id);
    }

    /**
     * Restituisce l'elenco completo di tutti gli utenti registrati nella piattaforma.
     */
    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {

        Page<UserEntity> usersPage = this.userRepository.findAll(pageable);
        return usersPage.map(this.userMapper::toDto);
    }
}
