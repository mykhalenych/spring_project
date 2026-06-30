package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.project.dto.BookDto;
import com.example.project.dto.BookSearchParametersDto;
import com.example.project.mapper.BookMapper;
import com.example.project.model.Book;
import com.example.project.repository.BookRepository;
import com.example.project.repository.SpecificationBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private SpecificationBuilder<Book> bookSpecificationBuilder;

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
        Book book = new Book();
        book.setId(1L);
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);

        when(bookSpecificationBuilder.build(searchParams)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.search(searchParams);

        assertEquals(1, actual.size());
        assertEquals(1L, actual.get(0).getId());
    }
}
