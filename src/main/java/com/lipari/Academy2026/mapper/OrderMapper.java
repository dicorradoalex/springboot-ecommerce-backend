package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.OrderResponseDTO;
import com.lipari.Academy2026.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderEntryMapper.class, UserMapper.class})
public interface OrderMapper {

    OrderEntity toEntity(OrderResponseDTO dto);
    OrderResponseDTO toDto(OrderEntity entity);
    List<OrderResponseDTO> toDtoList(List<OrderEntity> entityList);

    @Mapping(target = "id", ignore = true)
    void updateEntityToDto(OrderResponseDTO dto, @MappingTarget OrderEntity entity);
}
