package com.example.beercatalogueapi.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeerDTO {
    @NotBlank(message = "The name of the beer cannot be blank")
    private String name;
    private String type;
    @DecimalMin(value = "0.0", inclusive = true, message = "The ABV cannot be less than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "The ABV cannot be more than 100")
    private float ABV;
    private String description;
    @NotBlank(message = "The manufacturer name cannot be blank")
    private String manufacturerName;

    public BeerDTO() {

    }

    public BeerDTO(String name, String type, float ABV, String description, String manufacturerName) {
        this.name = name;
        this.type = type;
        this.ABV = ABV;
        this.description = description;
        this.manufacturerName = manufacturerName;
    }
}
