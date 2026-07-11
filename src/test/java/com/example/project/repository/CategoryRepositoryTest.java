package com.example.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.project.model.Category;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindCategory_ValidCategory_ReturnsCategory() {
        Category category = new Category();
        category.setName("Science");
        category.setDescription("Science books");

        Category saved = categoryRepository.save(category);
        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Science", found.get().getName());
        assertEquals("Science books", found.get().getDescription());
    }
}
