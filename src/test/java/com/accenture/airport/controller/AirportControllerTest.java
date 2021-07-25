package com.accenture.airport.controller;

import com.accenture.airport.model.AirportWithRunways;
import com.accenture.airport.service.AirportServiceImpl;
import com.accenture.airport.util.TestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.NoResultException;
import java.util.List;

import static com.accenture.airport.util.TestFactory.AIRPORTS;
import static com.accenture.airport.util.TestFactory.getCountriesModelList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class AirportControllerTest {

  @Mock private AirportServiceImpl airportService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(new AirportReportController(airportService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  public void testGetAirportsWithRunwaysWithCountryCode() throws Exception {
    Page<AirportWithRunways> airportWithRunways = new PageImpl(TestFactory.getListAirports(), PageRequest.of(0, 20), TestFactory.getListAirports().size());
    when(airportService.getAllAirportsWithRunwaysGivenCountry(anyString(), any(Pageable.class))).thenReturn(airportWithRunways);
    this.mockMvc.perform(get(AIRPORTS+"/country/US")).andExpect(status().isOk());
  }

  @Test
  public void testGetAirportsWithRunwaysWithCountryCodeNoContent() throws Exception {
    when(airportService.getAllAirportsWithRunwaysGivenCountry(anyString(), any(Pageable.class))).thenReturn(Page.empty());
    this.mockMvc.perform(get(AIRPORTS+"/country/TR")).andExpect(status().isNoContent());
  }

  @Test
  public void testGetAirportsWithRunwaysWithCountryCodeThrowsBadRequest() throws Exception {
    when(airportService.getAllAirportsWithRunwaysGivenCountry(anyString(), any(Pageable.class))).thenThrow(NoResultException.class);
    this.mockMvc.perform(get(AIRPORTS+"/country/BR")).andExpect(status().isBadRequest());

    when(airportService.getAllAirportsWithRunwaysGivenCountry(anyString(), any(Pageable.class))).thenThrow(IllegalArgumentException.class);
    this.mockMvc.perform(get(AIRPORTS+"/country/BR")).andExpect(status().isBadRequest());
  }

  @Test
  public void testGetAirportsWithRunwaysWithCountryCodeThrowsInternalServerError() throws Exception {
    when(airportService.getAllAirportsWithRunwaysGivenCountry(anyString(), any(Pageable.class))).thenThrow(RuntimeException.class);
    this.mockMvc.perform(get(AIRPORTS+"/country/BR")).andExpect(status().isInternalServerError());
  }

  @Test
  public void testGetTop10HighestNumberOfAirportCountries() throws Exception {
    when(airportService.getTopTenCountriesHasHighestNumberOfAirports()).thenReturn(getCountriesModelList());
    this.mockMvc.perform(get(AIRPORTS+"/top10")).andExpect(status().isOk());
  }

  @Test
  public void testGetTop10HighestNumberOfAirportCountriesNoContent() throws Exception {
    when(airportService.getTopTenCountriesHasHighestNumberOfAirports()).thenReturn(List.of());
    this.mockMvc.perform(get(AIRPORTS+"/top10")).andExpect(status().isNoContent());
  }

  @Test
  public void testGetTop10HighestNumberOfAirportCountriesThrowsInternalServerError() throws Exception {
    when(airportService.getTopTenCountriesHasHighestNumberOfAirports()).thenThrow(RuntimeException.class);
    this.mockMvc.perform(get(AIRPORTS+"/top10")).andExpect(status().isInternalServerError());
  }
}
