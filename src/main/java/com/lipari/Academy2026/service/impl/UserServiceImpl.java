package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.UserResponseDTO;
import com.lipari.Academy2026.dto.UserUpdateRequestDTO;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.UserMapper;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    // Dipendenze
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Recupera il profilo dell'utente attualmente loggato tramite il token JWT.
     */
    @Override
    public UserResponseDTO getCurrentUser() {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return this.userMapper.toDto(currentUser);
    }

    /**
     * Aggiorna i dati del profilo dell'utente loggato.
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(UserUpdateRequestDTO updateDTO) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Optional<UserEntity> userOpt = this.userRepository.findById(currentUser.getId());

        if (!userOpt.isPresent()) {
            throw new ResourceNotFoundException("Utente loggato non trovato nel database.");
        }

        UserEntity userToUpdate = userOpt.get();
        this.userMapper.updateEntityFromUpdateDto(updateDTO, userToUpdate);
        UserEntity savedUser = this.userRepository.save(userToUpdate);
        
        return this.userMapper.toDto(savedUser);
    }



    // AREA ADMIN

    @Override
    public UserResponseDTO getUser(UUID id) {
        Optional<UserEntity> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent())
            return this.userMapper.toDto(userOptional.get());
        else
            throw new ResourceNotFoundException("Utente con ID: " + id + " non trovato.");
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        Optional<UserEntity> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent())
            this.userRepository.deleteById(id);
        else throw new ResourceNotFoundException("Utente con ID: " + id + " non trovato.");
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> usersList = this.userRepository.findAll();
        return this.userMapper.toDtoList(usersList);
    }
}
