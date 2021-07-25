package com.accenture.airport.service;

import com.accenture.airport.entity.Airport;
import com.accenture.airport.entity.Country;
import com.accenture.airport.entity.Runway;
import com.accenture.airport.mapper.CountryMapper;
import com.accenture.airport.model.AirportWithRunways;
import com.accenture.airport.model.CountryModel;
import com.accenture.airport.repository.AirportRepository;
import com.accenture.airport.repository.CountryRepository;
import com.accenture.airport.repository.RunwayRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AirportServiceImpl implements AirportService {

  private final AirportRepository airportRepository;

  private final RunwayRepository runwayRepository;

  private final CountryRepository countryRepository;

  private final CountryMapper countryMapper;

  public AirportServiceImpl(
      AirportRepository airportRepository,
      RunwayRepository runwayRepository,
      CountryRepository countryRepository,
      CountryMapper countryMapper) {
    this.airportRepository = airportRepository;
    this.runwayRepository = runwayRepository;
    this.countryRepository = countryRepository;
    this.countryMapper = countryMapper;
  }

  public Page<AirportWithRunways> getAllAirportsWithRunwaysGivenCountry(
      String countryCodeOrName, Pageable pageable) {
    String countryCode = getCountryCode(countryCodeOrName);

    List<Airport> airports = airportRepository.findByIsoCountry(countryCode);
    int start = (int) pageable.getOffset();
    int end =
        (start + pageable.getPageSize()) > airports.size()
            ? airports.size()
            : (start + pageable.getPageSize());

    if (start > end) {
      throw new NoResultException(String.format("No record found for given page number %s", pageable.getPageNumber()));
    }

    List<Airport> paginatedAirports = airports.subList(start, end);
    List<Long> airport_refList =
        paginatedAirports.stream().map(x -> x.getId()).collect(Collectors.toList());

    List<Runway> runwaysOfAirports = runwayRepository.findByAirportRefIn(airport_refList);

    List<AirportWithRunways> airportWithRunwaysList =
        paginatedAirports.stream()
            .map(
                airport -> {
                  List<Runway> collect =
                      runwaysOfAirports.stream()
                          .filter(runway -> runway.getAirportRef() == airport.getId())
                          .collect(Collectors.toList());
                  return AirportWithRunways.builder().airport(airport).runways(collect).build();
                })
            .collect(Collectors.toList());

    return new PageImpl<>(airportWithRunwaysList, pageable, airports.size());
  }

  public List<CountryModel> getTopTenCountriesHasHighestNumberOfAirports() {
    List<String> airportCountries = airportRepository.getTop10CountryCodeWithHighestNumberOfAirports();
    List<Country> countries = countryRepository.findByCodeIn(airportCountries);

    Map<String, Country> countryHashMap = new HashMap<>();
    for (Country i : countries) countryHashMap.put(i.getCode(), i);

    return countryMapper.toModelCountryModelList(
            airportCountries.stream().map(x -> countryHashMap.get(x)).collect(Collectors.toList()));
  }

  private final String getCountryCode(String countryCodeOrName) {
    final String trimmedCountryCodeOrName = countryCodeOrName.trim();
    if (trimmedCountryCodeOrName.length() < 2) {
      throw new IllegalArgumentException(String.format("Invalid country code %s", countryCodeOrName));
    }

    return (trimmedCountryCodeOrName.length() == 2)
        ? countryCodeOrName.toUpperCase()
        : countryRepository.findByNameIsLike(countryCodeOrName)
            .map(country -> country.getCode())
            .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid country name %s", countryCodeOrName)));
  }
}
