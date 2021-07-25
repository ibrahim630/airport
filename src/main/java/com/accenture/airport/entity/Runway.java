package com.accenture.airport.entity;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "runway")
public class Runway {

  @Id @Parsed private Long id;

  @Parsed(field = "airport_ref")
  private Long airportRef;

  @Parsed
  private String airport_ident;

  @Parsed private String length_ft;
  @Parsed private String width_ft;
  @Parsed private String surface;
  @Parsed private String lighted;
  @Parsed private String closed;
  @Parsed private String le_ident;
  @Parsed private String le_latitude_deg;
  @Parsed private String le_longitude_deg;
  @Parsed private String le_elevation_ft;
  @Parsed private String le_heading_degT;
  @Parsed private String le_displaced_threshold_ft;
  @Parsed private String he_ident;
  @Parsed private String he_latitude_deg;
  @Parsed private String he_longitude_deg;
  @Parsed private String he_elevation_ft;
  @Parsed private String he_heading_degT;
  @Parsed private String he_displaced_threshold_ft;
}
