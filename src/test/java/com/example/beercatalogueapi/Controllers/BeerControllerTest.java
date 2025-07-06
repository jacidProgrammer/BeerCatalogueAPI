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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.beercatalogueapi.DTO.BeerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BeerControllerTest {

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
    void getBeers_returnsOkAndList() throws Exception {
        mockMvc.perform(get("/api/beer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createBeer_andGetBeerByName() throws Exception {
        // Create a new manufacturer
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestManufacturer\", \"country\":\"TestCountry\"}"))
                .andExpect(status().isOk());

        // Create a new beer
        BeerDTO beer = new BeerDTO("TestBeer", "Lager", 5.0f, "TestDesc", "TestManufacturer");
        mockMvc.perform(post("/api/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestBeer")));

        // Obtener cerveza por nombre
        mockMvc.perform(get("/api/beer/TestBeer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestBeer")));
    }

    @Test
    void updateBeer_returnsOk() throws Exception {
        // Create a new manufacturer
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestManufacturerUpdate\", \"country\":\"TestCountry\"}"))
                .andExpect(status().isOk());

        // Create a new beer
        BeerDTO beer = new BeerDTO("TestBeerUpdate", "Lager", 5.0f, "TestDesc", "TestManufacturerUpdate");
        mockMvc.perform(post("/api/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestBeerUpdate")));

        beer = new BeerDTO("TestBeerUpdate", "Ale", 6.0f, "UpdatedDesc", "TestManufacturerUpdate");
        mockMvc.perform(put("/api/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestBeerUpdate")))
                .andExpect(jsonPath("$.type", is("Ale")))
                .andExpect(jsonPath("$.abv", is(6.0)))
                .andExpect(jsonPath("$.description", is("UpdatedDesc")));
    }

    @Test
    void deleteBeer_returnsNoContent() throws Exception {
        // Create a new manufacturer
        mockMvc.perform(post("/api/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestManufacturerDelete\", \"country\":\"TestCountry\"}"))
                .andExpect(status().isOk());

        // Create a new beer
        BeerDTO beer = new BeerDTO("TestBeerDelete", "Lager", 5.0f, "TestDesc", "TestManufacturerDelete");
        mockMvc.perform(post("/api/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestBeerDelete")));

        // Delete the beer
        mockMvc.perform(delete("/api/beer/TestBeerDelete"))
                .andExpect(status().isOk());
    }
}