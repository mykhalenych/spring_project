package com.example.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.project.dto.CreateBookRequestDto;
import com.example.project.dto.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBook_ValidDto_ReturnsCreated() throws Exception {
        // 1. Create a category first
        CreateCategoryRequestDto catRequest = new CreateCategoryRequestDto();
        catRequest.setName("Tech");
        catRequest.setDescription("Technical books");

        MvcResult catResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String catResponseStr = catResult.getResponse().getContentAsString();
        Long categoryId = objectMapper.readTree(catResponseStr).get("id").asLong();

        // 2. Create a book referencing this category
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Effective Java");
        request.setAuthor("Joshua Bloch");
        request.setIsbn("978-0-13-468599-1");
        request.setPrice(BigDecimal.valueOf(45.00));
        request.setCategoryIds(List.of(categoryId));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.categoryIds[0]").value(categoryId));
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookById_ValidId_ReturnsBook() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setIsbn("978-0-13-235088-4");
        request.setPrice(BigDecimal.valueOf(39.99));

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long bookId = objectMapper.readTree(responseStr).get("id").asLong();

        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void updateBook_ValidIdAndDto_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setIsbn("978-0-13-235088-4");
        request.setPrice(BigDecimal.valueOf(39.99));

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long bookId = objectMapper.readTree(responseStr).get("id").asLong();

        CreateBookRequestDto updateRequest = new CreateBookRequestDto();
        updateRequest.setTitle("Clean Code Updated");
        updateRequest.setAuthor("Robert C. Martin");
        updateRequest.setIsbn("978-0-13-235088-4");
        updateRequest.setPrice(BigDecimal.valueOf(42.00));

        mockMvc.perform(put("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code Updated"))
                .andExpect(jsonPath("$.price").value(42.00));
    }

    @Test
    void deleteBook_ValidId_ReturnsNoContent() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Design Patterns");
        request.setAuthor("Gang of Four");
        request.setIsbn("978-0-201-63361-0");
        request.setPrice(BigDecimal.valueOf(49.99));

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long bookId = objectMapper.readTree(responseStr).get("id").asLong();

        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent());

        // Verification: getBookById should return 404 since it's soft-deleted
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookById_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }
}
