package com.accenture.airport.model;

import lombok.Data;

@Data
public class CountryModel {
    private Long id;
    private String code;
    private String name;
    private String continent;
    private String wikipedia_link;
    private String keywords;
}
