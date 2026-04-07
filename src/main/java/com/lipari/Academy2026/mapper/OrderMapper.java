package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.OrderDTO;
import com.lipari.Academy2026.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderEntryMapper.class, UserMapper.class})
public interface OrderMapper {

    OrderEntity toEntity(OrderDTO dto);
    OrderDTO toDto(OrderEntity entity);
    List<OrderDTO> toDtoList(List<OrderEntity> entityList);

    @Mapping(target = "id", ignore = true)
    void updateEntityToDto(OrderDTO dto, @MappingTarget OrderEntity entity);
}
