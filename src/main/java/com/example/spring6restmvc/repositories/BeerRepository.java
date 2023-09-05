package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
}
