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

import com.example.beercatalogueapi.DTO.ManufacturerDTO;
import com.example.beercatalogueapi.Entity.ManufacturerEntity;
import com.example.beercatalogueapi.Repository.ManufacturerRepository;

class ManufacturerServiceTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllManufacturers_returnsSortedList() {
        ManufacturerEntity m1 = new ManufacturerEntity("Zeta", "Spain");
        ManufacturerEntity m2 = new ManufacturerEntity("Alpha", "Germany");
        when(manufacturerRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<ManufacturerDTO> result = manufacturerService.getAllManufacturers();

        assertEquals(2, result.size());
        assertEquals("Alpha", result.get(0).getName());
        assertEquals("Zeta", result.get(1).getName());
    }

    @Test
    void getManufacturerByName_found() {
        ManufacturerEntity entity = new ManufacturerEntity("Mahou", "Spain");
        when(manufacturerRepository.findByName("Mahou")).thenReturn(entity);

        ManufacturerDTO dto = manufacturerService.getManufacturerByName("Mahou");

        assertEquals("Mahou", dto.getName());
        assertEquals("Spain", dto.getCountry());
    }

    @Test
    void getManufacturerByName_notFound_throws() {
        when(manufacturerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> manufacturerService.getManufacturerByName("Unknown"));
    }

    @Test
    void createManufacturer_success() {
        ManufacturerDTO dto = new ManufacturerDTO("Estrella", "Spain");
        when(manufacturerRepository.findByName("Estrella")).thenReturn(null);

        ManufacturerDTO result = manufacturerService.createManufacturer(dto);

        assertEquals("Estrella", result.getName());
        verify(manufacturerRepository).save(any(ManufacturerEntity.class));
    }

    @Test
    void createManufacturer_alreadyExists_throws() {
        ManufacturerDTO dto = new ManufacturerDTO("Mahou", "Spain");
        when(manufacturerRepository.findByName("Mahou")).thenReturn(new ManufacturerEntity());

        assertThrows(IllegalArgumentException.class, () -> manufacturerService.createManufacturer(dto));
    }

    @Test
    void updateManufacturer_success() {
        ManufacturerDTO dto = new ManufacturerDTO("Mahou", "France");
        ManufacturerEntity entity = new ManufacturerEntity("Mahou", "Spain");
        when(manufacturerRepository.findByName("Mahou")).thenReturn(entity);

        ManufacturerDTO result = manufacturerService.updateManufacturer(dto);

        assertEquals("Mahou", result.getName());
        assertEquals("France", result.getCountry());
        verify(manufacturerRepository).save(entity);
    }

    @Test
    void updateManufacturer_notFound_throws() {
        ManufacturerDTO dto = new ManufacturerDTO("Unknown", "Italy");
        when(manufacturerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> manufacturerService.updateManufacturer(dto));
    }

    @Test
    void deleteManufacturerByName_success() {
        ManufacturerEntity entity = new ManufacturerEntity("Mahou", "Spain");
        when(manufacturerRepository.findByName("Mahou")).thenReturn(entity);

        manufacturerService.deleteManufacturerByName("Mahou");

        verify(manufacturerRepository).deleteByName("Mahou");
    }

    @Test
    void deleteManufacturerByName_notFound_throws() {
        when(manufacturerRepository.findByName("Unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> manufacturerService.deleteManufacturerByName("Unknown"));
    }
}