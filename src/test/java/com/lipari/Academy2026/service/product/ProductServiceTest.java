package com.lipari.Academy2026.service.product;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di Unità per ProductServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Recupero prodotto: deve restituire il DTO se il prodotto esiste")
    void getProduct_ShouldReturnDto_WhenProductExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        ProductEntity entity = new ProductEntity();
        ProductResponseDTO expectedDto = new ProductResponseDTO(id, "Laptop", BigDecimal.ZERO, "Desc", 10, null);

        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDto(entity)).thenReturn(expectedDto);

        // WHEN
        ProductResponseDTO result = productService.getProduct(id);

        // THEN
        assertNotNull(result);
        assertEquals("Laptop", result.name());
        verify(productRepository).findById(id);
    }

    @Test
    @DisplayName("Creazione prodotto: deve associare correttamente la categoria")
    void createProduct_ShouldAssociateCategoryAndSave() {
        // GIVEN
        UUID categoryId = UUID.randomUUID();
        ProductRequestDTO request = new ProductRequestDTO("Mouse", new BigDecimal("25.00"), "Desc", 10, categoryId);
        
        ProductEntity productEntity = new ProductEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        
        ProductEntity savedProduct = new ProductEntity();
        ProductResponseDTO expectedDto = new ProductResponseDTO(UUID.randomUUID(), "Mouse", BigDecimal.TEN, "Desc", 10, null);

        when(productMapper.toEntity(request)).thenReturn(productEntity);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(productRepository.save(productEntity)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(expectedDto);

        // WHEN
        ProductResponseDTO result = productService.createProduct(request);

        // THEN
        assertNotNull(result);
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).save(productEntity);
        // Verifichiamo che la categoria sia stata effettivamente settata nell'entità prima del salvataggio
        assertEquals(categoryEntity, productEntity.getCategory());
    }

    @Test
    @DisplayName("Aggiornamento prodotto: deve cambiare categoria se richiesto")
    void updateProduct_ShouldChangeCategory_WhenIdIsDifferent() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        UUID oldCategoryId = UUID.randomUUID();
        UUID newCategoryId = UUID.randomUUID();
        
        ProductRequestDTO request = new ProductRequestDTO("Laptop Pro", new BigDecimal("1200"), "Desc", 5, newCategoryId);
        
        CategoryEntity oldCategory = new CategoryEntity();
        oldCategory.setId(oldCategoryId);
        
        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setId(newCategoryId);
        
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setCategory(oldCategory);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productMapper.toDto(any())).thenReturn(new ProductResponseDTO(productId, "Laptop Pro", BigDecimal.ZERO, "Desc", 5, null));

        // WHEN
        productService.updateProduct(request, productId);

        // THEN
        verify(productMapper).updateEntityFromRequest(eq(request), eq(existingProduct));
        assertEquals(newCategory, existingProduct.getCategory());
        verify(productRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Ricerca prodotti: deve restituire una pagina di DTO")
    void getAllProducts_ShouldReturnPagedDtos() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        ProductEntity p1 = new ProductEntity();
        ProductEntity p2 = new ProductEntity();
        Page<ProductEntity> productPage = new PageImpl<>(List.of(p1, p2));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        // Il mapper viene chiamato tramite il .map() della Page nel service

        // WHEN
        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Eliminazione prodotto: deve lanciare 404 se non esiste")
    void deleteProduct_ShouldThrow404_WhenNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(id));
        verify(productRepository, never()).delete(any());
    }
}
