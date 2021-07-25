package com.accenture.airport.entity;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Country {

  @Id @Parsed private Long id;
  @Parsed private String code;
  @Parsed private String name;
  @Parsed private String continent;
  @Parsed private String wikipedia_link;
  @Parsed private String keywords;
}
