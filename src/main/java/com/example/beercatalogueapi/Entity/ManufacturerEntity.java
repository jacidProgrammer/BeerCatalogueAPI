package com.example.beercatalogueapi.Entity;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "manufacturer")
public class ManufacturerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String country;

    public ManufacturerEntity() {}

    public ManufacturerEntity(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
