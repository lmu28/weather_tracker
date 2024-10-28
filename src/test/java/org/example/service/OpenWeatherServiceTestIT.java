package org.example.service;

import org.example.service.dto.LocationDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenWeatherServiceTestIT {


    public OpenWeatherService openWeatherService = new OpenWeatherService();


    @Test
    void getLocationByName_ValidName_ReturnsLocationDTO() {
        String name = "London";
        LocationDTO locationDTO = openWeatherService.getLocationByName(name);
        assertThat(locationDTO).isInstanceOf(LocationDTO.class);
    }

    @Test
    void getLocationByName_InvalidName_ReturnsNull() {
        String name = "null";
        LocationDTO locationDTO = openWeatherService.getLocationByName(name);
        assertThat(locationDTO).isNull();
    }
}