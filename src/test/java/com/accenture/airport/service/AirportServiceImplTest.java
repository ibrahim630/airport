package com.accenture.airport.service;

import com.accenture.airport.entity.Airport;
import com.accenture.airport.mapper.CountryMapper;
import com.accenture.airport.model.AirportWithRunways;
import com.accenture.airport.model.CountryModel;
import com.accenture.airport.repository.AirportRepository;
import com.accenture.airport.repository.CountryRepository;
import com.accenture.airport.repository.RunwayRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

import static com.accenture.airport.util.TestFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirportServiceImplTest {

  @Mock private AirportRepository airportRepository;

  @Mock private RunwayRepository runwayRepository;

  @Mock private CountryRepository countryRepository;

  @Mock private CountryMapper countryMapper;

  @InjectMocks private AirportServiceImpl airportService;

  @Test
  public void testGetCountriesHasHighestNumberOfAirports() {
    when(airportRepository.getTop10CountryCodeWithHighestNumberOfAirports()).thenReturn(getListAirportCountriesCode());
    when(countryRepository.findByCodeIn(anyList())).thenReturn(getCountriesList());
    when(countryMapper.toModelCountryModelList(anyList())).thenCallRealMethod();

    List<CountryModel> result = airportService.getTopTenCountriesHasHighestNumberOfAirports();
    assertEquals(result.size(), 2);
    verify(countryMapper).toModelCountryModelList(anyList());
  }

  @Test
  public void testGetAllAirportsWithRunwaysGivenCountry() {
    when(airportRepository.findByIsoCountry(anyString())).thenReturn(getListAirports());
    when(runwayRepository.findByAirportRefIn(anyList())).thenReturn(getRunways());
    Page<Airport> airportPage =
        new PageImpl<>(getListAirports(), PageRequest.of(0, 20), getListAirports().size());
    Page<AirportWithRunways> result =
        airportService.getAllAirportsWithRunwaysGivenCountry("US", airportPage.getPageable());

    assertEquals(result.getTotalElements(), 2);
    verify(runwayRepository).findByAirportRefIn(anyList());
  }

  @Test
  public void testGetAllAirportsWithRunwaysGivenCountryThrowsIllegalArgumentException() {
    Page<Airport> airportPage =
            new PageImpl<>(getListAirports(), PageRequest.of(0, 20), getListAirports().size());

    Assertions.assertThrows(IllegalArgumentException.class, ()->
            airportService.getAllAirportsWithRunwaysGivenCountry("U", airportPage.getPageable()));

    when(countryRepository.findByNameIsLike(anyString())).thenReturn(Optional.empty());
    Assertions.assertThrows(IllegalArgumentException.class, ()->
            airportService.getAllAirportsWithRunwaysGivenCountry("Alice Wonderland", airportPage.getPageable()));
  }

  @Test
  public void testGetAllAirportsWithRunwaysGivenCountryThrowsNoResultException() {
    Page<Airport> airportPage = new PageImpl(getListAirports(), PageRequest.of(1, 1), 1);

    Assertions.assertThrows(
        NoResultException.class,
        () ->
            airportService.getAllAirportsWithRunwaysGivenCountry("ZM", airportPage.getPageable()));
  }
}
