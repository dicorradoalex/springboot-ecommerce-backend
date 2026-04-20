package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.category.CategoryRequestDTO;
import com.lipari.Academy2026.dto.category.CategoryResponseDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Converte un'entità categoria nel DTO di risposta per il client.
     */
    CategoryResponseDTO toDto(CategoryEntity entity);

    /**
     * Converte il DTO di richiesta in un'entità categoria per la creazione.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productsList", ignore = true)
    CategoryEntity toEntity(CategoryRequestDTO dto);

    /**
     * Trasforma una lista di entità categoria in una lista di DTO di risposta.
     */
    List<CategoryResponseDTO> toDtoList(List<CategoryEntity> entities);

    /**
     * Aggiorna un'entità categoria esistente utilizzando i dati di un DTO di richiesta.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productsList", ignore = true)
    void updateEntityFromRequest(CategoryRequestDTO dto, @MappingTarget CategoryEntity entity);
}
