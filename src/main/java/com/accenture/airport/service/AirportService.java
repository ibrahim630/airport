package com.accenture.airport.service;

import com.accenture.airport.model.AirportWithRunways;
import com.accenture.airport.model.CountryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AirportService {

    List<CountryModel> getTopTenCountriesHasHighestNumberOfAirports();

    Page<AirportWithRunways> getAllAirportsWithRunwaysGivenCountry(String countryCodeOrName, Pageable pageable);
}
