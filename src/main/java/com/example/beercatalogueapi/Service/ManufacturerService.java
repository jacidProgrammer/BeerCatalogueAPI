package com.example.beercatalogueapi.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beercatalogueapi.DTO.ManufacturerDTO;
import com.example.beercatalogueapi.Entity.ManufacturerEntity;
import com.example.beercatalogueapi.Repository.ManufacturerRepository;

import jakarta.transaction.Transactional;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<ManufacturerDTO> getAllManufacturers() {
        return manufacturerRepository.findAll()
                .stream()
                .map(manufacturer -> new ManufacturerDTO(manufacturer.getName(), manufacturer.getCountry()))
                .sorted(Comparator.comparing(ManufacturerDTO::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public ManufacturerDTO getManufacturerByName(String name) {
        ManufacturerEntity entity = manufacturerRepository.findByName(name);
        if(entity == null) {
            throw new IllegalArgumentException("Manufacturer with this name doesn't exist");
        }
        return new ManufacturerDTO(entity.getName(), entity.getCountry());
    }

    @Transactional
    public ManufacturerDTO createManufacturer(ManufacturerDTO manufacturerDTO) {
        if( manufacturerRepository.findByName(manufacturerDTO.getName()) != null) {
            throw new IllegalArgumentException("Manufacturer with this name already exists");
        }
        ManufacturerEntity entity = new ManufacturerEntity(manufacturerDTO.getName(), manufacturerDTO.getCountry());
        manufacturerRepository.save(entity);
        return new ManufacturerDTO(entity.getName(), entity.getCountry());
    }

    @Transactional
    public ManufacturerDTO updateManufacturer(ManufacturerDTO manufacturerDTO) {
        ManufacturerEntity entity = manufacturerRepository.findByName(manufacturerDTO.getName());
        if(entity == null) {
            throw new IllegalArgumentException("Manufacturer with this name doesn't exist");
        }
        entity.setName(manufacturerDTO.getName());
        entity.setCountry(manufacturerDTO.getCountry());
        manufacturerRepository.save(entity);
        return new ManufacturerDTO(entity.getName(), entity.getCountry());
    }

    @Transactional
    public void deleteManufacturerByName(String name) {
        ManufacturerEntity entity = manufacturerRepository.findByName(name);
        if(entity == null) {
            throw new IllegalArgumentException("Manufacturer with this name doesn't exist");
        }
        manufacturerRepository.deleteByName(entity.getName());
    }
}
