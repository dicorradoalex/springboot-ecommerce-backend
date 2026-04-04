package com.lipari.Academy2026.service.impl;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import com.lipari.Academy2026.exception.ResourceNotFoundException;
import com.lipari.Academy2026.mapper.ProductMapper;
import com.lipari.Academy2026.repository.ProductRepository;
import com.lipari.Academy2026.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {


    // Componenti utilizzati dal Service -> final per constructor injection
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Constructor Injection
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO newProduct(ProductDTO productDTO) {
        // DTO -> Entity
        ProductEntity product = this.productMapper.toEntity(productDTO);
        // Salvo la nuova entity nel db
        product = this.productRepository.save(product);
        // Restituisco ciò che ho salvato
        return this.productMapper.toDto(product);
    }

    @Override
    public ProductDTO getProduct(String id) {
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

    @Override
    public void deleteProduct(String id) {
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

    /*

    @Override
    public ProductDTO deleteProduct(String id) throws Exception {
        // Chiedi al Repository di recuperare il prodotto tramite ID
        Optional<ProductEntity> productOptional = this.productRepository.findById(id);
        // Se lo trovo
        if(productOptional.isPresent()) {
            // Trasformo in DTO prima di cancellarlo
            ProductDTO deletedProduct = this.productMapper.toDto(productOptional.get());
            // Cancello l'entità dal db
            this.productRepository.delete(result.get());
            // Restituisco il DTO che ho cancellato
            return deletedProduct;
        } else {
            throw new ResourceNotFoundException("Prodotto con ID " + id + " non trovato");
        }
    }
    */


    @Override
    public ProductDTO modifyProduct(ProductDTO productDTO) {
        // Chiedi al repository di cercare il DTO nel db
        Optional<ProductEntity> productOptional = this.productRepository.findById(productDTO.getId());
        // Se presente
        if(productOptional.isPresent()) {
            // Estrai dall'Optional l'entità
            ProductEntity productToUpdate = productOptional.get();
            // Aggiorna l'entità utilizzando come valori quelli del DTO ricevuto come argomento
            productToUpdate.setName(productDTO.getName());
            // Salvo l'entità aggiornata
            ProductEntity savedProduct = this.productRepository.save(productToUpdate);
            // Converto l'oggetto salvato in DTO e lo restituisco al Controller
            return this.productMapper.toDto(savedProduct);
        }
        else {
            throw new ResourceNotFoundException("Prodotto con ID " + productDTO.getId() + " non trovato");
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        // Recupero le entità dal db
        List<ProductEntity> peList = this.productRepository.findAll();

        // Trasformo da Entity a DTO e restituisco
        return this.productMapper.toDtoList(peList);
    }

}
