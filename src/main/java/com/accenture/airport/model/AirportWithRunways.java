package com.accenture.airport.model;

import com.accenture.airport.entity.Airport;
import com.accenture.airport.entity.Runway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportWithRunways {
    Airport airport;
    List<Runway> runways;
}
