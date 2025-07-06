package com.example.beercatalogueapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beercatalogueapi.Entity.BeerEntity;

@Repository
public interface BeerRepository extends JpaRepository<BeerEntity, Long> {

    BeerEntity findByName(String name);

    void deleteByName(String name);
}
