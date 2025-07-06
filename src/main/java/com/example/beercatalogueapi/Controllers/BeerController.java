package com.example.beercatalogueapi.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.beercatalogueapi.DTO.BeerDTO;
import com.example.beercatalogueapi.Service.BeerService;

@RestController
public class BeerController {

    @Autowired
    private BeerService beerService;

    @GetMapping("/api/beer")
    public List<BeerDTO> getBeers() {
        return beerService.getAllBeers();
    }

    @GetMapping("/api/beer/{name}")
    public BeerDTO getBeerByName(@PathVariable String name) {
        return beerService.getBeerByName(name);
    }

    @PostMapping("/api/beer")
    public BeerDTO createBeer(@RequestBody BeerDTO beerDTO) {
        return beerService.createBeer(beerDTO);
    }

    @PutMapping("/api/beer")
    public BeerDTO updateBeer(@RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerDTO);
    }

    @DeleteMapping("/api/beer/{name}")
    public void deleteBeer(@PathVariable String name) {
        beerService.deleteBeerByName(name);
    }
}
