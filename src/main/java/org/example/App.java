package org.example;

import org.example.model.Location;
import org.example.repository.LocationRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.LocationService;
import org.example.service.OpenWeatherService;
import org.example.service.UserService;
import org.example.service.dto.LocationDTO;
import org.example.util.CryptUtil;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    static OpenWeatherService openWeatherService = new OpenWeatherService();
    static UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());
    static LocationService locationService = new LocationService(openWeatherService, userService, new LocationRepositoryHibernate());

    public static void main(String[] args) {

        List<String> list = Arrays.asList(
                "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
                "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
                "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte",
                "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington",
                "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City",
                "Portland", "Las Vegas", "Memphis", "Louisville", "Baltimore"
        );
        for (String locationName: list){
            LocationDTO ldto = openWeatherService.getLocationByName(locationName.toLowerCase());

            if (ldto != null){
                Location locationCur = new Location(ldto.getName(),ldto.getCoord().getLat(), ldto.getCoord().getLon());
                locationService.save(locationCur);
            }else {
                System.out.println(locationName);
            }
        }

    }


}
