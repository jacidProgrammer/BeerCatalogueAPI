package com.example.beercatalogueapi.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.beercatalogueapi.DTO.BeerDTO;
import com.example.beercatalogueapi.Entity.BeerEntity;
import com.example.beercatalogueapi.Entity.ManufacturerEntity;
import com.example.beercatalogueapi.Repository.BeerRepository;
import com.example.beercatalogueapi.Repository.ManufacturerRepository;

class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private BeerService beerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBeers_returnsSortedList() {
        ManufacturerEntity m = new ManufacturerEntity("Mahou", "Spain");
        BeerEntity b1 = new BeerEntity("Zeta", "Lager", 5.0f, "Desc", m);
        BeerEntity b2 = new BeerEntity("Alpha", "Ale", 6.0f, "Desc", m);
        when(beerRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<BeerDTO> result = beerService.getAllBeers();

        assertEquals(2, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Zeta", result.get(1).getName());
    }

    @Test
    void getBeerByName_found() {
        ManufacturerEntity m = new ManufacturerEntity("Mahou", "Spain");
        BeerEntity b = new BeerEntity("Mahou Cinco Estrellas", "Lager", 5.5f, "Classic", m);
        when(beerRepository.findByName("Mahou Cinco Estrellas")).thenReturn(b);

        BeerDTO dto = beerService.getBeerByName("Mahou Cinco Estrellas");

        assertEquals("Mahou Cinco Estrellas", dto.getName());
        assertEquals("Lager", dto.getType());
        assertEquals(5.5, dto.getABV());
        assertEquals("Classic", dto.getDescription());
        assertEquals("Mahou", dto.getManufacturerName());
    }

    @Test
    void getBeerByName_notFound_throws() {
        when(beerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> beerService.getBeerByName("Unknown"));
    }

    @Test
    void createBeer_success() {
        BeerDTO dto = new BeerDTO("Estrella", "Pilsner", 4.8f, "Smooth", "Damm");
        ManufacturerEntity m = new ManufacturerEntity("Damm", "Spain");
        when(beerRepository.findByName("Estrella")).thenReturn(null);
        when(manufacturerRepository.findByName("Damm")).thenReturn(m);

        BeerDTO result = beerService.createBeer(dto);

        assertEquals("Estrella", result.getName());
        verify(beerRepository).save(any(BeerEntity.class));
    }

    @Test
    void createBeer_alreadyExists_throws() {
        BeerDTO dto = new BeerDTO("Mahou", "Lager", 5.5f, "Classic", "Mahou");
        when(beerRepository.findByName("Mahou")).thenReturn(new BeerEntity());

        assertThrows(IllegalArgumentException.class, () -> beerService.createBeer(dto));
    }

    @Test
    void createBeer_manufacturerNotFound_throws() {
        BeerDTO dto = new BeerDTO("NewBeer", "Ale", 6.0f, "Fruity", "Unknown");
        when(beerRepository.findByName("NewBeer")).thenReturn(null);
        when(manufacturerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> beerService.createBeer(dto));
    }

    @Test
    void updateBeer_success() {
        ManufacturerEntity m = new ManufacturerEntity("Damm", "Spain");
        BeerEntity b = new BeerEntity("Estrella", "Pilsner", 4.8f, "Smooth", m);
        BeerDTO dto = new BeerDTO("Estrella", "IPA", 5.0f, "Bitter", "Damm");
        when(beerRepository.findByName("Estrella")).thenReturn(b);
        when(manufacturerRepository.findByName("Damm")).thenReturn(m);

        BeerDTO result = beerService.updateBeer(dto);

        assertEquals("Estrella", result.getName());
        assertEquals("IPA", result.getType());
        assertEquals(5.0, result.getABV());
        assertEquals("Bitter", result.getDescription());
        assertEquals("Damm", result.getManufacturerName());
        verify(beerRepository).save(b);
    }

    @Test
    void updateBeer_notFound_throws() {
        BeerDTO dto = new BeerDTO("Unknown", "Ale", 6.0f, "Fruity", "Damm");
        when(beerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> beerService.updateBeer(dto));
    }

    @Test
    void updateBeer_manufacturerNotFound_throws() {
        BeerEntity b = new BeerEntity("Estrella", "Pilsner", 4.8f, "Smooth", null);
        BeerDTO dto = new BeerDTO("Estrella", "IPA", 5.0f, "Bitter", "Unknown");
        when(beerRepository.findByName("Estrella")).thenReturn(b);
        when(manufacturerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> beerService.updateBeer(dto));
    }

    @Test
    void deleteBeerByName_success() {
        ManufacturerEntity m = new ManufacturerEntity("Mahou", "Spain");
        BeerEntity b = new BeerEntity("Mahou", "Lager", 5.5f, "Classic", m);
        when(beerRepository.findByName("Mahou")).thenReturn(b);

        beerService.deleteBeerByName("Mahou");

        verify(beerRepository).deleteByName("Mahou");
    }

    @Test
    void deleteBeerByName_notFound_throws() {
        when(beerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> beerService.deleteBeerByName("Unknown"));
    }
}