package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.CategoryDTO;
import com.lipari.Academy2026.entity.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // Permette a Spring di gestire il Mapper come un suo componente (Bean). Questo permette di poterlo utilizzare da altre parti senza fare new CategoryMapperImpl()
public interface CategoryMapper {

    CategoryDTO toDto(CategoryEntity pEntity);

    CategoryEntity toEntity(CategoryDTO pDto);

    List<CategoryDTO> toDtoList(List<CategoryEntity> entityList);

    List<CategoryEntity> toEntityList(List<CategoryDTO> dtoList);
}
