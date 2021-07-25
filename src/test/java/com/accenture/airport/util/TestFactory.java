package com.accenture.airport.util;

import com.accenture.airport.entity.Airport;
import com.accenture.airport.entity.Country;
import com.accenture.airport.entity.Runway;
import com.accenture.airport.model.CountryModel;

import java.util.List;

public class TestFactory {

  public static final String AIRPORTS = "/airports";

  public static List<String> getListAirportCountriesCode() {
    return List.of("US", "BR");
  }

  public static List<Country> getCountriesList() {
    return List.of(
        Country.builder().id(1l).name("United States").code("US").build(),
        Country.builder().id(2l).name("Brazil").code("BR").build());
  }

  public static List<CountryModel> getCountriesModelList() {
    CountryModel countryUS = new CountryModel();
    countryUS.setCode("US");
    countryUS.setName("United States");

    CountryModel countryBR = new CountryModel();
    countryBR.setCode("BR");
    countryBR.setName("Brazil");

    return List.of(countryUS, countryBR);
  }

  public static List<Airport> getListAirports() {
    return List.of(
        Airport.builder().id(1l).name("JFK").isoCountry("US").build(),
        Airport.builder().id(2l).name("IST").isoCountry("TR").build());
  }

  public static List<Runway> getRunways(){
    return List.of(
            Runway.builder().airportRef(1l).airport_ident("JFK_R1").build(),
            Runway.builder().airportRef(1l).airport_ident("JFK_R2").build(),
            Runway.builder().airportRef(2l).airport_ident("IST_R2").build(),
            Runway.builder().airportRef(2l).airport_ident("IST_R2").build()
    );
  }
}
