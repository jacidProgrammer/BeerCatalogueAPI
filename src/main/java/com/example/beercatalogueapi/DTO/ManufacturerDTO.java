package com.example.beercatalogueapi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManufacturerDTO {
    @NotBlank(message = "The name of the manufacturer cannot be blank")
    private String name;
    private String country;

    public ManufacturerDTO() {}

    public ManufacturerDTO(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
