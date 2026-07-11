package com.example.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.project.dto.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategory_ValidDto_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto();
        request.setName("Science Fiction");
        request.setDescription("Sci-fi books");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Science Fiction"))
                .andExpect(jsonPath("$.description").value("Sci-fi books"));
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto();
        request.setName("Adventure");
        request.setDescription("Adventure books");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long categoryId = objectMapper.readTree(responseStr).get("id").asLong();

        mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Adventure"));
    }

    @Test
    void updateCategory_ValidDto_ReturnsUpdatedCategory() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto();
        request.setName("Adventure");
        request.setDescription("Adventure books");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long categoryId = objectMapper.readTree(responseStr).get("id").asLong();

        CreateCategoryRequestDto updateRequest = new CreateCategoryRequestDto();
        updateRequest.setName("Action & Adventure");
        updateRequest.setDescription("Action/Adventure books");

        mockMvc.perform(put("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action & Adventure"))
                .andExpect(jsonPath("$.description").value("Action/Adventure books"));
    }

    @Test
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto();
        request.setName("History");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long categoryId = objectMapper.readTree(responseStr).get("id").asLong();

        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isNoContent());

        // Getting it again should return not found
        mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooksByCategoryId_ValidId_ReturnsList() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto();
        request.setName("Drama");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        Long categoryId = objectMapper.readTree(responseStr).get("id").asLong();

        mockMvc.perform(get("/categories/" + categoryId + "/books"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());
    }
}
