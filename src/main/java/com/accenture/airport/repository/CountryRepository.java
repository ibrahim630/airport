package com.accenture.airport.repository;

import com.accenture.airport.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository  extends JpaRepository<Country, Long> {

    Optional<Country> findByNameIsLike(String name);

    List<Country> findByCodeIn(List<String> code);
}
