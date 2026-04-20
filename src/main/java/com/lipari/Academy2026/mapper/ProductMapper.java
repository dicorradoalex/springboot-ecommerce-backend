package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.product.ProductRequestDTO;
import com.lipari.Academy2026.dto.product.ProductResponseDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    /**
     * Converte l'entità prodotto nel relativo DTO di risposta per il client.
     */
    ProductResponseDTO toDto(ProductEntity entity);

    /**
     * Converte il DTO di richiesta in un'entità prodotto per la creazione.
     * Nota: L'associazione della categoria viene gestita nel Service.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    /**
     * Trasforma una lista di entità prodotto in una lista di DTO di risposta.
     */
    List<ProductResponseDTO> toDtoList(List<ProductEntity> entityList);

    /**
     * Aggiorna un'entità prodotto esistente utilizzando i dati di un DTO di richiesta.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}
