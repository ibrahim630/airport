package com.accenture.airport.mapper;

import com.accenture.airport.entity.Country;
import com.accenture.airport.model.CountryModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CountryMapper {

  public CountryModel toModel(Country country) {
    CountryModel model = new CountryModel();
    BeanUtils.copyProperties(country, model);

    return model;
  }

  public List<CountryModel> toModelCountryModelList(List<Country> countries) {
    return countries.stream().map(country -> toModel(country)).collect(Collectors.toList());
  }
}
