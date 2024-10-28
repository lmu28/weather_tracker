package org.example.service;

import org.example.model.Location;
import org.example.service.dto.LocationDTO;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

   private final OpenWeatherService openWeatherService = new OpenWeatherService();


    public List<Location> findByUserId(long userId){
        return null;
    }

    public List<LocationDTO> getCurrentTemps(List<Location> locations){
       List<LocationDTO> locationDTOS = new ArrayList<>();
        for (Location location: locations){
            String name = location.getName();
            LocationDTO locationDTO = openWeatherService.getLocationByName(name);
            if (locationDTO != null){
                locationDTOS.add(locationDTO);
            }else {
                // delete this user-location raw
            }

        }
        return locationDTOS;


    }
}
