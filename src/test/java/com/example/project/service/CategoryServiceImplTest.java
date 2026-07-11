package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.project.dto.CategoryDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.CategoryMapper;
import com.example.project.model.Category;
import com.example.project.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void save_ValidDto_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Drama");

        Category category = new Category();
        category.setName("Drama");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Drama");

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryDto result = categoryService.save(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void getById_ValidId_ReturnsCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Drama");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Drama");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void getById_InvalidId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(1L));
    }

    @Test
    void findAll_ValidPageable_ReturnsCategoryDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        category.setId(1L);
        category.setName("Drama");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Drama");

        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(1, result.size());
        assertEquals("Drama", result.get(0).getName());
    }

    @Test
    void update_ValidIdAndDto_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Updated Name");

        Category category = new Category();
        category.setId(1L);
        category.setName("Drama");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Updated Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryDto result = categoryService.update(1L, requestDto);

        assertEquals(responseDto, result);
        verify(categoryMapper, times(1)).updateCategoryFromDto(requestDto, category);
    }

    @Test
    void update_InvalidId_ThrowsEntityNotFoundException() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(1L, requestDto));
    }

    @Test
    void deleteById_ValidId_PerformsDeletion() {
        categoryService.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
