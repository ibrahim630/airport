package com.accenture.airport.controller;

import com.accenture.airport.model.AirportWithRunways;
import com.accenture.airport.model.CountryModel;
import com.accenture.airport.service.AirportServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.NoResultException;
import java.util.List;

@Validated
@Log4j2
@Controller
@RequestMapping("/airports")
public class AirportReportController {

  private final AirportServiceImpl airportService;

  public AirportReportController(AirportServiceImpl airportService) {
    this.airportService = airportService;
  }

  @GetMapping("/country/{countryCodeOrName}")
  public ResponseEntity getAirportsWithRunwaysWithCountryCode(
          @PathVariable String countryCodeOrName, @PageableDefault Pageable pageable) {
    try {
      Page<AirportWithRunways> allAirportsWithRunwaysGivenCountry =
          airportService.getAllAirportsWithRunwaysGivenCountry(countryCodeOrName, pageable);

      if (allAirportsWithRunwaysGivenCountry.isEmpty()) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity(allAirportsWithRunwaysGivenCountry, HttpStatus.OK);
    } catch (NoResultException | IllegalArgumentException e) {
      String message =
          "Could not find any airport with given arguments"
              + e.getMessage();
      log.error(message, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      log.error("Something went wrong during call /country/{}", countryCodeOrName, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/top10")
  public ResponseEntity getTop10HighestNumberOfAirportCountries() {
    try {
      List<CountryModel> allAirportWithRunways =
          airportService.getTopTenCountriesHasHighestNumberOfAirports();

      if (allAirportWithRunways.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(allAirportWithRunways, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Something went wrong during call /top10", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
