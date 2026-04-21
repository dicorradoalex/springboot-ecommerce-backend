package com.lipari.Academy2026.service.product;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

/**
 * Implementazione del servizio per la gestione del catalogo prodotti.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class ProductServiceImpl implements ProductService {

    // DIPENDENZE
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Recupera i dettagli di un singolo prodotto tramite il suo ID.
     */
    @Override
    public ProductResponseDTO getProduct(UUID id) {

        // Cerco il prodotto nel database
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (productOpt.isEmpty())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        // Restituisco il DTO mappato
        return this.productMapper.toDto(productOpt.get());
    }

    /**
     * Restituisce l'elenco completo di tutti i prodotti presenti nel catalogo.
     */
    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {

        // Recupero la pagina di entità prodotto dal db
        Page<ProductEntity> productsPage = this.productRepository.findAll(pageable);

        // Uso .map() della Page per convertire ogni entità in DTO
        return productsPage.map(this.productMapper::toDto);
    }

    /**
     * Cerca i prodotti nel catalogo filtrandoli per nome (case-insensitive).
     */
    @Override
    public Page<ProductResponseDTO> searchProductsByName(String name, Pageable pageable) {

        // Eseguo la ricerca tramite repository
        Page<ProductEntity> productsPage = this.productRepository.findByNameContainingIgnoreCase(name, pageable);

        // Uso .map() della Page per convertire ogni entità in DTO
        return productsPage.map(this.productMapper::toDto);
    }

    /**
     * Recupera l'elenco dei prodotti appartenenti a una specifica categoria.
     */
    @Override
    public Page<ProductResponseDTO> getProductsByCategory(String categoryName, Pageable pageable) {

        // Recupero i prodotti filtrati per nome categoria dal repository
        Page<ProductEntity> productsPage = this.productRepository.findByCategory_NameIgnoreCase(categoryName, pageable);

        // Uso .map() della Page per convertire ogni entità in DTO
        return productsPage.map(this.productMapper::toDto);
    }



    /**
     * Recupera l'elenco dei prodotti appartenenti a una specifica categoria tramite ID.
     */
    @Override
    public Page<ProductResponseDTO> getProductsByCategoryId(UUID categoryId, Pageable pageable) {

        // Recupero i prodotti filtrati per ID categoria dal repository
        Page<ProductEntity> productsPage = this.productRepository.findByCategory_Id(categoryId, pageable);

        // Uso .map() della Page per convertire ogni entità in DTO
        return productsPage.map(this.productMapper::toDto);
    }



    // AREA ADMIN

    /**
     * Inserisce un nuovo prodotto nel catalogo.
     */
    @Transactional
    @Override
    public ProductResponseDTO createProduct(@Valid ProductRequestDTO requestDTO) {

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

        if (productOpt.isEmpty())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        // Rimuovo l'entità dal database
        this.productRepository.delete(productOpt.get());
    }

    /**
     * Aggiorna le informazioni di un prodotto esistente.
     */
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(@Valid ProductRequestDTO requestDTO, UUID id) {

        // Recupero il prodotto tramite ID
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (productOpt.isEmpty())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        ProductEntity productToUpdate = productOpt.get();

        // Aggiorno i campi dell'entità tramite il mapper
        this.productMapper.updateEntityFromRequest(requestDTO, productToUpdate);

        // Salvo e restituisco il DTO aggiornato
        ProductEntity savedProduct = this.productRepository.save(productToUpdate);
        return this.productMapper.toDto(savedProduct);
    }
}
