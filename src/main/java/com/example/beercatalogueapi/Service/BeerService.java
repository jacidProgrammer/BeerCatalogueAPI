package com.example.beercatalogueapi.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beercatalogueapi.DTO.BeerDTO;
import com.example.beercatalogueapi.Entity.BeerEntity;
import com.example.beercatalogueapi.Entity.ManufacturerEntity;
import com.example.beercatalogueapi.Repository.BeerRepository;
import com.example.beercatalogueapi.Repository.ManufacturerRepository;

import jakarta.transaction.Transactional;

@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<BeerDTO> getAllBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beer -> new BeerDTO(beer.getName(), beer.getType(), beer.getABV(), beer.getDescription(), beer.getManufacturer().getName()))
                .sorted(Comparator.comparing(BeerDTO::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public BeerDTO getBeerByName(String name) {
        BeerEntity beerEntity = beerRepository.findByName(name);
        if( beerEntity == null) {
            throw new IllegalArgumentException("Beer with this name doesn't exist");
        }
        return new BeerDTO(beerEntity.getName(), beerEntity.getType(), beerEntity.getABV(), beerEntity.getDescription(), beerEntity.getManufacturer().getName());
    }

    @Transactional
    public BeerDTO createBeer(BeerDTO beerDTO) {
        if( beerRepository.findByName(beerDTO.getName()) != null) {
            throw new IllegalArgumentException("Beer with this name already exists");
        }
        ManufacturerEntity manufacturerEntity = manufacturerRepository.findByName(beerDTO.getManufacturerName());
        if( manufacturerEntity == null) {
            throw new IllegalArgumentException("Manufacturer with this name doesn't exist");
        }
        BeerEntity entity = new BeerEntity(beerDTO.getName(), beerDTO.getType(), beerDTO.getABV(), beerDTO.getDescription(), manufacturerEntity);
        beerRepository.save(entity);
        return new BeerDTO(entity.getName(), entity.getType(), entity.getABV(), entity.getDescription(), entity.getManufacturer().getName());
    }

    @Transactional
    public BeerDTO updateBeer(BeerDTO beerDTO) {
        BeerEntity entity = beerRepository.findByName(beerDTO.getName());
        if(entity == null) {
            throw new IllegalArgumentException("Beer with this name doesn't exist");
        }
        entity.setName(beerDTO.getName());
        entity.setType(beerDTO.getType());
        entity.setABV(beerDTO.getABV());
        entity.setDescription(beerDTO.getDescription());
        ManufacturerEntity manufacturerEntity = manufacturerRepository.findByName(beerDTO.getManufacturerName());
        if(manufacturerEntity == null) {
            throw new IllegalArgumentException("Manufacturer with this name doesn't exist");
        }
        entity.setManufacturer(manufacturerEntity);
        beerRepository.save(entity);
        return new BeerDTO(entity.getName(), entity.getType(), entity.getABV(), entity.getDescription(), entity.getManufacturer().getName());
    }

    @Transactional
    public void deleteBeerByName(String name) {
        BeerEntity entity = beerRepository.findByName(name);
        if(entity == null) {
            throw new IllegalArgumentException("Beer with this name doesn't exist");
        }
        beerRepository.deleteByName(entity.getName());
    }
}
