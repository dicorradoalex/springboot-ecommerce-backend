package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.ProductRequestDTO;
import com.lipari.Academy2026.dto.ProductResponseDTO;
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
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    // Dipendenze
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    /**
     * Recupera un singolo prodotto dal database tramite ID.
     */
    @Override
    public ProductResponseDTO getProduct(UUID id) {
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);

        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");
        }

        return this.productMapper.toDto(productOptional.get());
    }

    /**
     * Crea un nuovo prodotto associandolo a una categoria esistente.
     */
    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Recupera la categoria nel database tramite l'ID fornito nel DTO
        Optional<CategoryEntity> categoryOpt = this.categoryRepository.findById(requestDTO.categoryId());

        if (!categoryOpt.isPresent())
            throw new ResourceNotFoundException("La categoria con ID " + requestDTO.categoryId() + " non esiste.");

        // Converto il DTO di richiesta in Entity
        ProductEntity product = this.productMapper.toEntity(requestDTO);

        // Associo manualmente la categoria trovata
        product.setCategory(categoryOpt.get());

        // Salvo e restituisco il DTO di risposta
        ProductEntity savedProduct = this.productRepository.save(product);
        return this.productMapper.toDto(savedProduct);
    }

    /**
     * Elimina un prodotto dal sistema.
     */
    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (!productOpt.isPresent())
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");

        this.productRepository.delete(productOpt.get());
    }

    /**
     * Aggiorna i dati di un prodotto esistente.
     */
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(ProductRequestDTO requestDTO, UUID id) {
        // Cerco il prodotto da aggiornare
        Optional<ProductEntity> productOpt = this.productRepository.findById(id);

        if (!productOpt.isPresent()) {
            throw new ResourceNotFoundException("Prodotto con ID: " + id + " non trovato");
        }

        ProductEntity productToUpdate = productOpt.get();

        // Gestione Categoria (Controllo se è cambiata)
        UUID currentCategoryId = productToUpdate.getCategory().getId();
        UUID newCategoryId = requestDTO.categoryId();

        if (!currentCategoryId.equals(newCategoryId)) {
            Optional<CategoryEntity> newCategoryOpt = this.categoryRepository.findById(newCategoryId);
            if (!newCategoryOpt.isPresent()) {
                throw new ResourceNotFoundException("Categoria con ID: " + newCategoryId + " non trovata.");
            }
            productToUpdate.setCategory(newCategoryOpt.get());
        }

        // Aggiorno gli altri campi tramite mapper
        this.productMapper.updateEntityFromRequest(requestDTO, productToUpdate);

        // Salvo e restituisco
        ProductEntity savedProduct = this.productRepository.save(productToUpdate);
        return this.productMapper.toDto(savedProduct);
    }

    /**
     * Recupera l'elenco completo dei prodotti.
     */
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductEntity> productList = this.productRepository.findAll();
        return this.productMapper.toDtoList(productList);
    }

    /**
     * Cerca i prodotti nel catalogo filtrandoli per nome.
     */
    @Override
    public List<ProductResponseDTO> searchProductsByName(String name) {
        // Chiedo al repository di trovare i prodotti che contengono la stringa cercata
        List<ProductEntity> productList = this.productRepository.findByNameContainingIgnoreCase(name);
        // Converto la lista di entità in DTO e la restituisco
        return this.productMapper.toDtoList(productList);
    }

    /**
     * Recupera tutti i prodotti che appartengono a una determinata categoria.
     */
    @Override
    public List<ProductResponseDTO> getProductsByCategory(String categoryName) {
        // Filtro i prodotti in base al nome della categoria associata
        List<ProductEntity> productList = this.productRepository.findByCategory_Name(categoryName);
        // Converto i risultati e li restituisco al chiamante
        return this.productMapper.toDtoList(productList);
    }
}