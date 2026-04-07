package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductDTO toDto(ProductEntity entity);

    ProductEntity toEntity(ProductDTO dto);

    List<ProductDTO> toDtoList(List<ProductEntity> entityList);

    /*
        updateEntityFromDto(...) permette di prendere i dati dal DTO e spalmarli nell'entità.
        Il vantaggio è che nel ServiceImpl puoi evitare di scrivere più righe di set come:

        ad esempio: productToUpdate.setName(productDTO.name());

        Le annotazioni:
            @MappingTarget -> messa prima di un parametro permette di sovrascrive i suoi campi
            con i valori dell'oggetto ricevuto come primo argomento.

            @Mapping(target = "colonna", ignore = true) -> si mette sopra il metodo e specifica
            che una data colonna non deve essere sovrascritta. Nota: l'ID non si sovrascrive mai
            perché è la PK del db, se per errore qualcuno invia un ID diverso nel DTO si rischia
            di cambiare anche l'ID dell'entità del db e l'oggetto si perde.

        Nota: Tipo di ritorno void perché l'oggetto dopo @MappingTarget viene modificato in memoria
        (per riferimento).

     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Momentaneo
    void updateEntityFromDto(ProductDTO dto, @MappingTarget ProductEntity entity);

}
