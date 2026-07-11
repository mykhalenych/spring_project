package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.project.model.Book;
import com.example.project.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAllByCategoryId_CategoryMapped_ReturnsMatchingBooks() {
        Category category = new Category();
        category.setName("Fiction");
        category = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Author Name");
        book.setIsbn("978-0-123456-47-9");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(Set.of(category));

        bookRepository.save(book);

        List<Book> books = bookRepository.findAllByCategoryId(category.getId());

        assertEquals(1, books.size());
        assertEquals("Sample Book", books.get(0).getTitle());
    }
}
