package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.OrderEntryResponseDTO;
import com.lipari.Academy2026.entity.OrderEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderEntryMapper {

    OrderEntryResponseDTO toDto(OrderEntryEntity entity);

    @Mapping(target = "order", ignore = true)
    OrderEntryEntity toEntity(OrderEntryResponseDTO dto);

    List<OrderEntryResponseDTO> toDtoList(List<OrderEntryEntity> entityList);
}
