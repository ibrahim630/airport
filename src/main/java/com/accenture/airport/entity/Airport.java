package com.accenture.airport.entity;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Airport {

  @Id @Parsed private long id;
  @Parsed private String ident;
  @Parsed private String type;
  @Parsed private String name;

  @Parsed(field = "iso_country")
  private String isoCountry;
}
