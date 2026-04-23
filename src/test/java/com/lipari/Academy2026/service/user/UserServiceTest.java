package com.lipari.Academy2026.service.user;

import com.lipari.Academy2026.dto.user.UserResponseDTO;
import com.lipari.Academy2026.dto.user.UserUpdateRequestDTO;
import com.lipari.Academy2026.entity.UserEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.UserMapper;
import com.lipari.Academy2026.repository.UserRepository;
import com.lipari.Academy2026.util.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per UserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Profilo corrente: deve restituire i dati dell'utente loggato")
    void getCurrentUser_ShouldReturnDtoOfLoggedInUser() {
        // GIVEN
        UserEntity currentUser = UserEntity.builder().id(UUID.randomUUID()).email("test@test.it").build();
        UserResponseDTO expectedDto = new UserResponseDTO(currentUser.getId(), currentUser.getEmail(), "Mario", "Rossi", null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(userMapper.toDto(currentUser)).thenReturn(expectedDto);

        // WHEN
        UserResponseDTO result = userService.getCurrentUser();

        // THEN
        assertNotNull(result);
        assertEquals("test@test.it", result.email());
        verify(securityUtils).getCurrentUser();
    }

    @Test
    @DisplayName("Aggiornamento utente: deve salvare le modifiche dell'utente loggato")
    void updateUser_ShouldSaveModifiedUser() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UserEntity loggedInUser = UserEntity.builder().id(userId).build();
        UserUpdateRequestDTO request = new UserUpdateRequestDTO("Luigi", "Verdi", "Via Roma", "Roma", "Italy");
        
        UserEntity userFromDb = new UserEntity();
        UserEntity savedUser = new UserEntity();
        UserResponseDTO expectedDto = new UserResponseDTO(userId, "test@test.it", "Luigi", "Verdi", "Via Roma", "Roma", "Italy");

        when(securityUtils.getCurrentUser()).thenReturn(loggedInUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));
        when(userRepository.save(userFromDb)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

        // WHEN
        UserResponseDTO result = userService.updateUser(request);

        // THEN
        assertNotNull(result);
        assertEquals("Luigi", result.name());
        verify(userMapper).updateEntityFromUpdateDto(eq(request), eq(userFromDb));
        verify(userRepository).save(userFromDb);
    }

    @Test
    @DisplayName("Recupero utente Admin: deve lanciare 404 se l'ID non esiste")
    void getUser_ShouldThrow404_WhenUserNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(id));
    }

    @Test
    @DisplayName("Eliminazione utente: deve procedere con soft delete se esiste")
    void deleteUser_ShouldCallDeleteById_WhenUserExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        // WHEN
        userService.deleteUser(id);

        // THEN
        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("Lista utenti: deve restituire pagina di utenti")
    void getAllUsers_ShouldReturnPagedResponse() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> userPage = new PageImpl<>(List.of(new UserEntity(), new UserEntity()));
        
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // WHEN
        Page<UserResponseDTO> result = userService.getAllUsers(pageable);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(userRepository).findAll(pageable);
    }
}
