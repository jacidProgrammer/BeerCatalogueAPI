package com.example.beercatalogueapi.Entity;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "beer")
public class BeerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String type;
    private float ABV;
    private String description;
    @ManyToOne(optional = false)
    private ManufacturerEntity manufacturer;

    public BeerEntity() {}

    public BeerEntity(String name, String type, float abv, String description, ManufacturerEntity manufacturerEntity) {
        this.name = name;
        this.type = type;
        this.ABV = abv;
        this.description = description;
        this.manufacturer = manufacturerEntity;
    }
}
