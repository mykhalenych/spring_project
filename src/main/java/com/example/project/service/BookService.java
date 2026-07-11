package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.BookWithoutCategoryIdsDto;
import com.example.project.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    List<BookWithoutCategoryIdsDto> findAllByCategoryId(Long categoryId);
}

