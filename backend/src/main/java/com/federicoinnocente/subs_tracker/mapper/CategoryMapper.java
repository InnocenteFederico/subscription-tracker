package com.federicoinnocente.subs_tracker.mapper;

import com.federicoinnocente.subs_tracker.dto.CategoryDTO;
import com.federicoinnocente.subs_tracker.entity.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(CategoryEntity categoryEntity);

    CategoryEntity toEntity(CategoryDTO categoryDTO);

    List<CategoryDTO> toDtoList(List<CategoryEntity> categoryEntityList);

}
