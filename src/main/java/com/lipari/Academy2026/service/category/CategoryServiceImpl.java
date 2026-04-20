package com.lipari.Academy2026.service.category;

import com.lipari.Academy2026.dto.category.CategoryRequestDTO;
import com.lipari.Academy2026.dto.category.CategoryResponseDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.CategoryMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementazione del servizio per la gestione delle categorie merceologiche.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    // DIPENDENZE
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Recupera una categoria specifica dal database tramite il suo ID.
     */
    @Override
    public CategoryResponseDTO getCategory(UUID id) {

        // Cerco la categoria tramite ID
        Optional<CategoryEntity> categoryOpt = this.categoryRepository.findById(id);
        
        if (!categoryOpt.isPresent())
            throw new ResourceNotFoundException("Categoria con ID: " + id + " non trovata");

        // Restituisco il DTO mappato
        return this.categoryMapper.toDto(categoryOpt.get());
    }

    /**
     * Restituisce l'elenco completo di tutte le categorie presenti a catalogo.
     */
    @Override
    public List<CategoryResponseDTO> getAllCategories() {

        // Recupero la lista di tutte le entità categoria
        List<CategoryEntity> categoriesList = this.categoryRepository.findAll();
        
        // Restituisco la lista mappata in DTO
        return this.categoryMapper.toDtoList(categoriesList);
    }



    // AREA ADMIN

    /**
     * Crea una nuova categoria di prodotti nel sistema.
     */
    @Transactional
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {

        // Converto il DTO in Entità
        CategoryEntity category = this.categoryMapper.toEntity(requestDTO);
        
        // Salvo la categoria e restituisco il DTO di risposta
        CategoryEntity savedCategory = this.categoryRepository.save(category);
        return this.categoryMapper.toDto(savedCategory);
    }

    /**
     * Elimina definitivamente una categoria dal database tramite ID.
     */
    @Transactional
    @Override
    public void deleteCategory(UUID id) {

        // Cerco l'entità da eliminare
        Optional<CategoryEntity> categoryOpt = this.categoryRepository.findById(id);
        
        if (!categoryOpt.isPresent())
            throw new ResourceNotFoundException("Categoria con ID: " + id + " non trovata");
        
        // Rimuovo definitivamente la categoria
        this.categoryRepository.delete(categoryOpt.get());
    }

    /**
     * Aggiorna le informazioni di una categoria esistente.
     */
    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(CategoryRequestDTO requestDTO, UUID id) {

        // Recupero la categoria tramite ID
        Optional<CategoryEntity> categoryOpt = this.categoryRepository.findById(id);
        
        if (!categoryOpt.isPresent())
            throw new ResourceNotFoundException("Categoria con ID: " + id + " non trovata");

        CategoryEntity categoryToUpdate = categoryOpt.get();

        // Aggiorno i dati dell'entità tramite il mapper
        this.categoryMapper.updateEntityFromRequest(requestDTO, categoryToUpdate);

        // Salvo le modifiche e restituisco il DTO aggiornato
        CategoryEntity savedCategory = this.categoryRepository.save(categoryToUpdate);
        return this.categoryMapper.toDto(savedCategory);
    }
}
