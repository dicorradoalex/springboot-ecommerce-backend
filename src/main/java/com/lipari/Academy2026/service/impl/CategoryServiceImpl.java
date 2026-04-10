package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.CategoryMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
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
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // DTO -> Entity
        CategoryEntity category = this.categoryMapper.toEntity(categoryDTO);
        // Salvo la nuova entity nel db
        category = this.categoryRepository.save(category);
        // Restituisco ciò che ho salvato
        return this.categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO getCategory(UUID id) {
        // Chiedo al repository di trovare l'entità
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
        // Se presente
        if (categoryOptional.isPresent()) {
            // Entity -> DTO e restituisco al Controller
            return this.categoryMapper.toDto(categoryOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione personalizzata
            throw new ResourceNotFoundException("Categoria con ID " + id + " non trovata.");
        }

    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        // Chiedi al Repository di recuperare il prodotto
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
        // Se lo trovo
        if (categoryOptional.isPresent()) {
            // Cancello l'entità dal db
            this.categoryRepository.delete(categoryOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione
            throw new ResourceNotFoundException("Categoria con ID " + id + " non trovata.");
        }
    }


    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        // Chiedi al repository di cercare il DTO nel db
        Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(categoryDTO.id());
        // Se presente
        if (categoryOptional.isPresent()) {
            // Estrai dall'Optional l'entità
            CategoryEntity categoryToUpdate = categoryOptional.get();
            // Aggiorna l'entità utilizzando come valori quelli del DTO ricevuto come argomento
            categoryToUpdate.setName(categoryDTO.name());
            // Salvo l'entità aggiornata
            CategoryEntity savedProduct = this.categoryRepository.save(categoryToUpdate);
            // Converto l'oggetto salvato in DTO e lo restituisco al Controller
            return this.categoryMapper.toDto(savedProduct);
        } else {
            throw new ResourceNotFoundException("Categoria con ID " + categoryDTO.id() + " non trovata.");
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

/*
    NOTE DIDATTICHE

    Annotazioni LOMBOK/SPRING (Configurazione)
    - @Service
      Indica che la classe contiene la "Business Logic". Spring la registra nel
      suo contesto per permetterne l'iniezione (DI) nei Controller.

    - @RequiredArgsConstructor
      Abilita la Constructor Injection per le dipendenze (Repository, Mapper).
      Garantisce che il Service sia immutabile e pronto all'uso.

    Gestione delle Transazioni (@Transactional)

    - Concetto Generale:
      Se un metodo fallisce a metà, Spring esegue il Rollback, annullando
      automaticamente ogni modifica fatta fino a quel momento per evitare dati corrotti.
      Va messa nei metodi che modificano il contenuto del db (creazione, modifica o
      cancellazione).

      Per evitare di scrivere @Transactional in tutti i metodi ho adottato questa strategia:

      1. Livello Classe: @Transactional(readOnly = true)
         Imposta l'intero servizio in "sola lettura". Questo ottimizza le
         performance del database e protegge i dati da scritture accidentali.

      2. Livello Metodo: @Transactional
         Viene aggiunto esplicitamente sui metodi di creazione, modifica o
         cancellazione per "aprire" i permessi di scrittura e sovrascrivere
         il limite imposto sulla classe.

    Logica di Business e Integrità
    - Gestione 404 (ResourceNotFoundException)
      Se un ID non esiste, lancia un'eccezione che verrà tradotta in
      un errore 404 per il client.

    - Gestione 409 (AlreadyExistsException)
      Prima di creare nuove risorse, il Service controlla eventuali duplicati
      (es. email o codici univoci) lanciando un'eccezione di conflitto.
-----------
*/
