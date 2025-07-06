package com.example.beercatalogueapi.Controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.beercatalogueapi.DTO.ManufacturerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ManufacturerControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("BeerCatalogueAPI_test")
            .withUsername("admin")
            .withPassword("admin");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getManufacturers_returnsOkAndList() throws Exception {
        mockMvc.perform(get("/api/manufacturer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createManufacturer_andGetByName() throws Exception {
        // Create a new manufacturer
        ManufacturerDTO manufacturer = new ManufacturerDTO("TestManufacturer", "TestCountry");
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestManufacturer")));

        // Get the created manufacturer by name
        mockMvc.perform(get("/api/manufacturer/TestManufacturer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestManufacturer")));
    }

    @Test
    void updateManufacturer_returnsOk() throws Exception {
        // First, create a manufacturer to update
        ManufacturerDTO initialManufacturer = new ManufacturerDTO("TestManufacturerUpdate", "TestCountry");
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialManufacturer)))
                .andExpect(status().isOk());

        // Now, update the manufacturer
        ManufacturerDTO manufacturer = new ManufacturerDTO("TestManufacturerUpdate", "UpdatedCountry");
        mockMvc.perform(put("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is("UpdatedCountry")));
    }

    @Test
    void deleteManufacturer_returnsOk() throws Exception {
        // First, create a manufacturer to delete
        ManufacturerDTO initialManufacturer = new ManufacturerDTO("TestManufacturerDelete", "TestCountry");
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialManufacturer)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/manufacturer/TestManufacturerDelete"))
                .andExpect(status().isOk());
    }
}