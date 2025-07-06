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

import com.example.beercatalogueapi.DTO.ManufacturerDTO;
import com.example.beercatalogueapi.Service.ManufacturerService;

@RestController
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping("/api/test")
    public boolean getTest() {
        return true;
    }

    @GetMapping("/api/manufacturer")
    public List<ManufacturerDTO> getManufacturers() {
        return manufacturerService.getAllManufacturers();
    }

    @GetMapping("/api/manufacturer/{name}")
    public ManufacturerDTO getManufacturerById(@PathVariable String name) {
        return manufacturerService.getManufacturerByName(name);
    }

    @PostMapping("/api/manufacturer")
    public ManufacturerDTO createManufacturer(@RequestBody ManufacturerDTO manufacturerDTO) {
        return manufacturerService.createManufacturer(manufacturerDTO);
    }

    @PutMapping("/api/manufacturer")
    public ManufacturerDTO updateManufacturer(@RequestBody ManufacturerDTO manufacturerDTO) {
        return manufacturerService.updateManufacturer(manufacturerDTO);
    }

    @DeleteMapping("/api/manufacturer/{name}")
    public void deleteManufacturer(@PathVariable String name) {
        manufacturerService.deleteManufacturerByName(name);
    }
}
