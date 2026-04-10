package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // Imposta tutto il service come sola lettura di default
public class ProductServiceImpl implements ProductService {


    // Componenti utilizzati dal Service -> final per constructor injection
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    // Constructor Injection
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // DTO -> Entity
        ProductEntity product = this.productMapper.toEntity(productDTO);
        // Estraggo l'ID della categoria dal DTO
        UUID id_category = productDTO.category().id();
        if(this.categoryRepository.findById(id_category).isPresent()) {
            // Salvo la nuova entity nel db
            product = this.productRepository.save(product);
            // Restituisco ciò che ho salvato
            return this.productMapper.toDto(product);
        }
        else {
            throw new ResourceNotFoundException("La categoria con ID " + id_category + " non è stata trovata.");
        }
    }

    @Override
    public ProductDTO getProduct(UUID id) {
        // Chiedo al repository di trovare l'entità
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);
        // Se presente
        if(productOptional.isPresent()) {
            // Entity -> DTO e restituisco al Controller
            return this.productMapper.toDto(productOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione
            throw new ResourceNotFoundException("Prodotto con ID " + id + " non trovato");
        }

    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        // Chiedi al Repository di recuperare il prodotto
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);
        // Se lo trovo
        if(productOptional.isPresent()) {
            // Cancello l'entità dal db
            this.productRepository.delete(productOptional.get());
        } else {
            // Se qualcosa non va lancia eccezione
            throw new ResourceNotFoundException("Prodotto con ID " + id + " non trovato");
        }
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        // Chiedi al repository di cercare il DTO nel db
        Optional<ProductEntity> productOptional = this.productRepository.findById(productDTO.id());
        // Se presente
        if(productOptional.isPresent()) {
            // Estrai dall'Optional l'entità
            ProductEntity productToUpdate = productOptional.get();
            // Aggiorna l'entità utilizzando come valori quelli del DTO ricevuto come argomento
            this.productMapper.updateEntityFromDto(productDTO, productToUpdate);
            // Salvo l'entità aggiornata
            ProductEntity savedProduct = this.productRepository.save(productToUpdate);
            // Converto l'oggetto salvato in DTO e lo restituisco al Controller
            return this.productMapper.toDto(savedProduct);
        }
        else {
            throw new ResourceNotFoundException("Prodotto con ID " + productDTO.id() + " non trovato");
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        // Recupero le entità dal db
        List<ProductEntity> productsList = this.productRepository.findAll();

        // Trasformo da Entity a DTO e restituisco
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
