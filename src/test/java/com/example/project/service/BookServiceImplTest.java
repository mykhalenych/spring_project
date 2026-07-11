package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.dto.BookWithoutCategoryIdsDto;
import com.example.project.dto.CreateBookRequestDto;
import com.example.project.mapper.BookMapper;
import com.example.project.model.Book;
import com.example.project.model.Category;
import com.example.project.repository.BookRepository;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.SpecificationBuilder;
import java.math.BigDecimal;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private SpecificationBuilder<Book> bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void search_ValidParams_ReturnsList() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                new String[]{"Title"},
                new String[]{"Author"},
                new String[]{"Isbn"}
        );
        Specification<Book> specification = (root, query, criteriaBuilder) -> null;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        book.setId(1L);
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);

        when(bookSpecificationBuilder.build(searchParams)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.search(searchParams, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals(1L, actual.getContent().get(0).getId());
    }

    @Test
    void save_ValidDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Title");
        requestDto.setCategoryIds(List.of(1L));

        Book book = new Book();
        book.setTitle("Book Title");

        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Book Title");
        bookDto.setCategoryIds(List.of(1L));

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(requestDto);

        assertEquals(bookDto, result);
    }

    @Test
    void findAllByCategoryId_ValidId_ReturnsList() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");

        BookWithoutCategoryIdsDto slimDto = new BookWithoutCategoryIdsDto();
        slimDto.setId(1L);
        slimDto.setTitle("Book Title");

        when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(slimDto);

        List<BookWithoutCategoryIdsDto> result = bookService.findAllByCategoryId(1L);

        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).getTitle());
    }
}
