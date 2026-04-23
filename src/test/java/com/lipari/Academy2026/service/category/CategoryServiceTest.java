package com.lipari.Academy2026.service.category;

import com.lipari.Academy2026.dto.category.CategoryRequestDTO;
import com.lipari.Academy2026.dto.category.CategoryResponseDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.exception.CategoryNotEmptyException;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.CategoryMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per CategoryServiceImpl.
 * Utilizziamo MockitoExtension per simulare le dipendenze (Repository e Mapper) senza avviare il database.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Recupero categoria: deve restituire il DTO se la categoria esiste")
    void getCategory_ShouldReturnDto_WhenCategoryExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        CategoryEntity entity = new CategoryEntity();
        CategoryResponseDTO expectedDto = new CategoryResponseDTO(id, "Elettronica");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(expectedDto);

        // WHEN
        CategoryResponseDTO result = categoryService.getCategory(id);

        // THEN
        assertNotNull(result);
        assertEquals("Elettronica", result.name());
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Creazione categoria: deve salvare e restituire il nuovo DTO")
    void createCategory_ShouldReturnSavedDto() {
        // GIVEN
        CategoryRequestDTO request = new CategoryRequestDTO("Libri");
        CategoryEntity entity = new CategoryEntity();
        CategoryEntity savedEntity = new CategoryEntity();
        CategoryResponseDTO expectedDto = new CategoryResponseDTO(UUID.randomUUID(), "Libri");

        when(categoryMapper.toEntity(request)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(savedEntity);
        when(categoryMapper.toDto(savedEntity)).thenReturn(expectedDto);

        // WHEN
        CategoryResponseDTO result = categoryService.createCategory(request);

        // THEN
        assertNotNull(result);
        assertEquals("Libri", result.name());
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName("Eliminazione categoria: deve procedere se la categoria esiste ed è vuota")
    void deleteCategory_ShouldDelete_WhenCategoryExistsAndIsEmpty() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(productRepository.existsByCategory_Id(id)).thenReturn(false);

        // WHEN
        categoryService.deleteCategory(id);

        // THEN
        verify(categoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("Eliminazione categoria: deve lanciare eccezione se la categoria contiene prodotti")
    void deleteCategory_ShouldThrowException_WhenCategoryIsNotEmpty() {
        // GIVEN
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(productRepository.existsByCategory_Id(categoryId)).thenReturn(true);

        // WHEN & THEN
        assertThrows(CategoryNotEmptyException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Eliminazione categoria: deve lanciare eccezione se la categoria non esiste")
    void deleteCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        // GIVEN
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
    }

    @Test
    @DisplayName("Aggiornamento categoria: deve aggiornare i dati se la categoria esiste")
    void updateCategory_ShouldReturnUpdatedDto() {
        // GIVEN
        UUID id = UUID.randomUUID();
        CategoryRequestDTO request = new CategoryRequestDTO("Elettronica Nuova");
        CategoryEntity existingEntity = new CategoryEntity();
        existingEntity.setId(id); // Importante per il controllo equals nel service
        
        CategoryEntity savedEntity = new CategoryEntity();
        CategoryResponseDTO expectedDto = new CategoryResponseDTO(id, "Elettronica Nuova");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(categoryRepository.save(existingEntity)).thenReturn(savedEntity);
        when(categoryMapper.toDto(savedEntity)).thenReturn(expectedDto);

        // WHEN
        CategoryResponseDTO result = categoryService.updateCategory(request, id);

        // THEN
        assertNotNull(result);
        assertEquals("Elettronica Nuova", result.name());
        verify(categoryMapper).updateEntityFromRequest(eq(request), any());
    }

    @Test
    @DisplayName("Lista categorie: deve restituire l'elenco completo")
    void getAllCategories_ShouldReturnList() {
        // GIVEN
        List<CategoryEntity> entities = List.of(new CategoryEntity(), new CategoryEntity());
        List<CategoryResponseDTO> dtos = List.of(
                new CategoryResponseDTO(UUID.randomUUID(), "Cat1"),
                new CategoryResponseDTO(UUID.randomUUID(), "Cat2")
        );

        when(categoryRepository.findAll()).thenReturn(entities);
        when(categoryMapper.toDtoList(entities)).thenReturn(dtos);

        // WHEN
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }
}
