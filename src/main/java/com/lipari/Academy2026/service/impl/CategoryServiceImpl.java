package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.mapper.CategoryMapper;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.CategoryService;
import com.lipari.Academy2026.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {


    // Componenti utilizzati dal Service -> final per constructor injection
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Constructor Injection
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // DTO -> Entity
        CategoryEntity category = this.categoryMapper.toEntity(categoryDTO);
        // Salvo la nuova entity nel db
        category = this.categoryRepository.save(category);
        // Restituisco ciò che ho salvato
        return this.categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO getCategory(UUID id) throws Exception {
        // Chiedo al repository di trovare l'entità
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
        // Se presente
        if(categoryOptional.isPresent()) {
            // Entity -> DTO e restituisco al Controller
            return this.categoryMapper.toDto(categoryOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione
            throw new Exception("Categoria non trovata");
        }

    }

    @Override
    public void deleteCategory(UUID id) throws Exception {
        // Chiedi al Repository di recuperare il prodotto
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
        // Se lo trovo
        if(categoryOptional.isPresent()) {
            // Cancello l'entità dal db
            this.categoryRepository.delete(categoryOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione
            throw new Exception("Categoria non trovata.");
        }
    }


    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) throws Exception {
        // Chiedi al repository di cercare il DTO nel db
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(categoryDTO.id());
        // Se presente
        if(categoryOptional.isPresent()) {
            // Estrai dall'Optional l'entità
            CategoryEntity categoryToUpdate = categoryOptional.get();
            // Aggiorna l'entità utilizzando come valori quelli del DTO ricevuto come argomento
            categoryToUpdate.setName(categoryDTO.name());
            // Salvo l'entità aggiornata
            CategoryEntity savedProduct = this.categoryRepository.save(categoryToUpdate);
            // Converto l'oggetto salvato in DTO e lo restituisco al Controller
            return this.categoryMapper.toDto(savedProduct);
        }
        else {
            throw new Exception("Categoria non trovata");
        }
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        // Recupero le entità dal db
        List<CategoryEntity> cList = this.categoryRepository.findAll();

        // Trasformo da Entity a DTO e restituisco
        return this.categoryMapper.toDtoList(cList);
    }

}
