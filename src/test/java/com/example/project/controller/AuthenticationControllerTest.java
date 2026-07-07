package com.example.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.project.dto.UserRegistrationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ValidRequest_ReturnsCreated() throws Exception {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("john.doe@example.com");
        request.setPassword("securePassword123");
        request.setRepeatPassword("securePassword123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setShippingAddress("123 Main St, City, Country");

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.shippingAddress").value("123 Main St, City, Country"));
    }

    @Test
    void register_PasswordMismatch_ReturnsBadRequest() throws Exception {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("john.doe@example.com");
        request.setPassword("securePassword123");
        request.setRepeatPassword("differentPassword");
        request.setFirstName("John");
        request.setLastName("Doe");

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_DuplicateEmail_ReturnsConflict() throws Exception {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto();
        request.setEmail("duplicate@example.com");
        request.setPassword("securePassword123");
        request.setRepeatPassword("securePassword123");
        request.setFirstName("John");
        request.setLastName("Doe");

        // First registration
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Second registration with the same email
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
