package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.ProductDTO;
import com.lipari.Academy2026.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductDTO toDto(ProductEntity pEntity);

    ProductEntity toEntity(ProductDTO pDto);

    List<ProductDTO> toDtoList(List<ProductEntity> pEntities);

}
