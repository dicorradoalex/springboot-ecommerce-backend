package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.UserDTO;
import com.lipari.Academy2026.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(UserEntity entity);
    UserEntity toEntity(UserDTO dto);
    List<UserDTO> toDtoList(List<UserEntity> entityList);

    @Mapping(target = "id", ignore = true)
    void updateToDto(UserDTO dto, @MappingTarget UserEntity entity);

}
