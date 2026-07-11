package com.example.project.mapper;

import com.example.project.config.MapperConfig;
import com.example.project.dto.CategoryDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.example.project.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toModel(CreateCategoryRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(
            CreateCategoryRequestDto requestDto,
            @MappingTarget Category category
    );
}
