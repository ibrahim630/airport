package com.accenture.airport.repository;

import com.accenture.airport.entity.Airport;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

  List<Airport> findByIsoCountry(String countryCode);

  @Query(
      value =
          "SELECT ISO_COUNTRY FROM AIRPORT GROUP BY ISO_COUNTRY ORDER BY COUNT(*) DESC limit 10",
      nativeQuery = true)
  List<String> getTop10CountryCodeWithHighestNumberOfAirports();

}
