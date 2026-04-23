package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.order.OrderEntryResponseDTO;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderEntryMapper {

    /**
     * Converte un'entità riga d'ordine nel relativo DTO di risposta.
     * Mappa l'ID del prodotto navigando nell'entità e il nome dal campo storicizzato.
     */
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "productName")
    OrderEntryResponseDTO toDto(OrderEntryEntity entity);

    /**
     * Converte il DTO di risposta in un'entità riga d'ordine.
     */
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderEntryEntity toEntity(OrderEntryResponseDTO dto);

    /**
     * Trasforma una lista di entità riga d'ordine in una lista di DTO di risposta.
     */
    List<OrderEntryResponseDTO> toDtoList(List<OrderEntryEntity> entityList);
}
