package com.example.project.controller;

import com.example.project.dto.BookWithoutCategoryIdsDto;
import com.example.project.dto.CategoryDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.example.project.service.BookService;
import com.example.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category Management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category", description = "Create and save a new category")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    @Operation(summary = "Get all categories",
            description = "Retrieve a list of categories with pagination")
    public List<CategoryDto> getAll(@ParameterObject Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID",
            description = "Retrieve a single category by its ID")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category",
            description = "Update category details by ID")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CreateCategoryRequestDto categoryDto
    ) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by ID",
            description = "Soft delete an existing category by ID")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category ID",
            description = "Retrieve a list of books belonging to a specific category")
    public List<BookWithoutCategoryIdsDto> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
