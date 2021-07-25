package com.accenture.airport.repository;

import com.accenture.airport.entity.Runway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunwayRepository extends JpaRepository<Runway, Long> {

    List<Runway> findByAirportRefIn(List<Long> airportRef );
}