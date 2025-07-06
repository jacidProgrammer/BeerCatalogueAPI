package com.example.beercatalogueapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beercatalogueapi.Entity.ManufacturerEntity;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Long> {

    ManufacturerEntity findByName(String name);

    void deleteByName(String name);
}
