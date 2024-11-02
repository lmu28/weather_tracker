package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepository;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.dto.LocationDTO;
import org.example.util.CryptUtil;
import org.example.util.FromKelvinToCelsiusConverter;
import org.example.util.TempConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class LocationService {

   private final OpenWeatherService openWeatherService;

   private final UserService userService;

   private final LocationRepository locationRepository;



    public List<LocationDTO> getCurrentTemps(User user){
       List<LocationDTO> locationDTOS = new ArrayList<>();
        for (Location location: user.getLocations()){
            String name = location.getName();
            LocationDTO locationDTO = openWeatherService.getLocationByName(name);
            if (locationDTO != null){
                TempConverter tempConverter = new FromKelvinToCelsiusConverter();
                double temp = locationDTO.getMain().getTemp();
                temp = tempConverter.convert(temp);
                locationDTO.getMain().setTemp(temp);
                locationDTOS.add(locationDTO);
            }else {
                userService.removeLocationFromUser(user, location);
                remove(location);
            }

        }
        return locationDTOS;
    }

    public Location searchLocation(String name){
        Location location = findByName(name);
        if (location == null){
            LocationDTO locationDTO = openWeatherService.getLocationByName(name);
            if (locationDTO !=  null){
                location = new Location(locationDTO.getName(), locationDTO.getCoord().getLat(), locationDTO.getCoord().getLon());
                save(location);
            }
        }
        return location;
    }

    private void remove(Location location) {
        locationRepository.remove(location);
    }

// TODO: test try save not unique lat lon
    public void save(Location location){
        locationRepository.save(location);
    }


    public Location findByName(String name){
        return locationRepository.findByName(name).orElse(null);
    }
}
