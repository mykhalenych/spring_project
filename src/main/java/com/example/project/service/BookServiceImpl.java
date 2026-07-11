package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.BookWithoutCategoryIdsDto;
import com.example.project.dto.CreateBookRequestDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.BookMapper;
import com.example.project.model.Book;
import com.example.project.model.Category;
import com.example.project.repository.BookRepository;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.SpecificationBuilder;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        setCategories(requestDto, book);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update book, id not found: " + id)
        );
        bookMapper.updateBookFromDto(requestDto, existingBook);
        setCategories(requestDto, existingBook);
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public java.util.List<BookWithoutCategoryIdsDto> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private void setCategories(CreateBookRequestDto requestDto, Book book) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = requestDto.getCategoryIds().stream()
                    .map(categoryRepository::findById)
                    .map(opt -> opt.orElseThrow(
                            () -> new EntityNotFoundException("Category not found")
                    ))
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        }
    }
}

