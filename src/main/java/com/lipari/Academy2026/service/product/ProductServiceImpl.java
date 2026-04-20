package com.lipari.Academy2026.service.product;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.CategoryRepository;
import com.lipari.Academy2026.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementazione del servizio per la gestione del catalogo prodotti.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    // DIPENDENZE
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    /**
     * Recupera i dettagli di un singolo prodotto tramite il suo ID.
     */
    @Override
    public ProductResponseDTO getProduct(UUID id) {

        // Cerco il prodotto nel database
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        // Restituisco il DTO mappato
        return this.productMapper.toDto(productOpt.get());
    }

    /**
     * Restituisce l'elenco completo di tutti i prodotti presenti nel catalogo.
     */
    @Override
    public List<ProductResponseDTO> getAllProducts() {

        // Recupero tutte le entità prodotto
        List<ProductEntity> productsList = this.productRepository.findAll();

        // Restituisco la lista mappata in DTO
        return this.productMapper.toDtoList(productsList);
    }

    /**
     * Cerca i prodotti nel catalogo filtrandoli per nome (case-insensitive).
     */
    @Override
    public List<ProductResponseDTO> searchProductsByName(String name) {

        // Eseguo la ricerca tramite repository
        List<ProductEntity> productsList = this.productRepository.findByNameContainingIgnoreCase(name);

        // Restituisco la lista mappata in DTO
        return this.productMapper.toDtoList(productsList);
    }

    /**
     * Recupera l'elenco dei prodotti appartenenti a una specifica categoria.
     */
    @Override
    public List<ProductResponseDTO> getProductsByCategory(String categoryName) {

        // Recupero i prodotti filtrati per nome categoria
        List<ProductEntity> productsList = this.productRepository.findByCategory_NameIgnoreCase(categoryName);

        // Restituisco la lista mappata in DTO
        return this.productMapper.toDtoList(productsList);
    }



    // AREA ADMIN

    /**
     * Inserisce un nuovo prodotto nel catalogo.
     */
    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        // Converto il DTO in Entità
        ProductEntity product = this.productMapper.toEntity(requestDTO);

        // Salvo il prodotto e restituisco il DTO di risposta
        ProductEntity savedProduct = this.productRepository.save(product);
        return this.productMapper.toDto(savedProduct);
    }

    /**
     * Rimuove definitivamente un prodotto dal catalogo tramite ID.
     */
    @Transactional
    @Override
    public void deleteProduct(UUID id) {

        // Cerco il prodotto da eliminare
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        // Rimuovo l'entità dal database
        this.productRepository.delete(productOpt.get());
    }

    /**
     * Aggiorna le informazioni di un prodotto esistente.
     */
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(ProductRequestDTO requestDTO, UUID id) {

        // Recupero il prodotto tramite ID
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        ProductEntity productToUpdate = productOpt.get();

        // Aggiorno i campi dell'entità tramite il mapper
        this.productMapper.updateEntityFromRequest(requestDTO, productToUpdate);

        // Salvo e restituisco il DTO aggiornato
        ProductEntity savedProduct = this.productRepository.save(productToUpdate);
        return this.productMapper.toDto(savedProduct);
    }
}
