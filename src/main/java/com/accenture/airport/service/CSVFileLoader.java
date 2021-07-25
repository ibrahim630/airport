package com.accenture.airport.service;

import com.accenture.airport.entity.Airport;
import com.accenture.airport.entity.Country;
import com.accenture.airport.entity.Runway;
import com.accenture.airport.repository.AirportRepository;
import com.accenture.airport.repository.CountryRepository;
import com.accenture.airport.repository.RunwayRepository;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
public class CSVFileLoader {

    private final AirportRepository airportRepository;

    private final CountryRepository countryRepository;

    private final RunwayRepository runwayRepository;

    @Autowired
    public CSVFileLoader(AirportRepository airportRepository, CountryRepository countryRepository, RunwayRepository runwayRepository) {
        this.airportRepository = airportRepository;
        this.countryRepository = countryRepository;
        this.runwayRepository = runwayRepository;
    }

    @PostConstruct
    public void init() {
        loadCSVFileToDB("csv_files/countries.csv", Country.class, countryRepository);
        loadCSVFileToDB("csv_files/airports.csv", Airport.class, airportRepository);
        loadCSVFileToDB("csv_files/runways.csv", Runway.class, runwayRepository);
    }

  public void loadCSVFileToDB(String fileName, Class beanClass, JpaRepository jpaRepository) {
    try (Reader inputReader =
        new InputStreamReader(
            new FileInputStream(
                this.getClass().getClassLoader().getSystemResource(fileName).getFile()),
            StandardCharsets.UTF_8)) {
      BeanListProcessor rowProcessor = new BeanListProcessor(beanClass);
      CsvParserSettings settings = new CsvParserSettings();
      settings.setHeaderExtractionEnabled(true);
      settings.setProcessor(rowProcessor);
      CsvParser parser = new CsvParser(settings);
      parser.parse(inputReader);
      jpaRepository.saveAll(rowProcessor.getBeans());
    } catch (IOException e) {
      log.error("Can not load {} CSV file.", fileName, e);
    }
  }
}
