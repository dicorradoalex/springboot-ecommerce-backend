package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.order.OrderResponseDTO;
import com.lipari.Academy2026.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderEntryMapper.class, UserMapper.class})
public interface OrderMapper {

    /**
     * Converte il DTO di risposta in un'entità ordine.
     */
    OrderEntity toEntity(OrderResponseDTO dto);

    /**
     * Converte l'entità ordine nel relativo DTO di risposta per il client.
     */
    OrderResponseDTO toDto(OrderEntity entity);

    /**
     * Trasforma una lista di entità ordine in una lista di DTO di risposta.
     */
    List<OrderResponseDTO> toDtoList(List<OrderEntity> entityList);

    /**
     * Aggiorna un'entità ordine esistente utilizzando i dati di un DTO di risposta.
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityToDto(OrderResponseDTO dto, @MappingTarget OrderEntity entity);
}
