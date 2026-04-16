package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Imposta tutto il service come sola lettura di default
public class ProductServiceImpl implements ProductService {


    // Dipendenze
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // DTO -> Entity
        ProductEntity product = this.productMapper.toEntity(productDTO);

        // Estraggo l'ID della categoria dal DTO
        UUID categoryId = productDTO.category().id();

        // Cerco la categoria nel database
        Optional<CategoryEntity> CategoryOptional = this.categoryRepository.findById(categoryId);
        if(!CategoryOptional.isPresent())
            throw new ResourceNotFoundException("La categoria con ID " + categoryId + " non è stata trovata.");

        // Assegno la categoria trovata nel prodotto
        product.setCategory(CategoryOptional.get());

        // Salvo e restituisco
        product = this.productRepository.save(product);
        return this.productMapper.toDto(product);
    }

    @Override
    public ProductDTO getProduct(UUID id) {
        // Recupera il prodotto dal database
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);
        if(!productOptional.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID " + id + " non trovato");

        // Restituisci il prodotto trovato
        return this.productMapper.toDto(productOptional.get());
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        // Recupera il prodotto dal database
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);
        if(!productOptional.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID " + id + " non trovato");

        // Cancella
        this.productRepository.delete(productOptional.get());
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {

        // Cerco il prodotto nel database
        Optional<ProductEntity> productOptional = this.productRepository.findById(productDTO.id());
        if (!productOptional.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID " + productDTO.id() + " non trovato");

        // Estraggo il prodotto da aggiornare che ho trovato
        ProductEntity productToUpdate = productOptional.get();

        // Gestione categoria
        UUID actualCategoryId = productToUpdate.getCategory().getId();
        UUID newCategoryId = productDTO.category().id();

        // Se le categorie sono diverse
        if (!actualCategoryId.equals(newCategoryId)) {
            // Cerco la nuova categoria nel database
            Optional<CategoryEntity> newCategoryOptional = this.categoryRepository.findById(newCategoryId);
            if (!newCategoryOptional.isPresent())
                throw new ResourceNotFoundException("Categoria con ID: " + newCategoryId + " non trovata.");
            // E la imposto come categoria del prodotto
            productToUpdate.setCategory(newCategoryOptional.get());
        }

        // Aggiorno gli altri campi dal DTO
        this.productMapper.updateEntityFromDto(productDTO, productToUpdate);

        // Salvo e Restituisco
        ProductEntity savedProduct = this.productRepository.save(productToUpdate);
        return this.productMapper.toDto(savedProduct);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        // Recupero le entità dal db
        List<ProductEntity> productsList = this.productRepository.findAll();

        // Entity -> DTO e restituisco
        return this.productMapper.toDtoList(productsList);
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
