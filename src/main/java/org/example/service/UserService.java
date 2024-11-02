package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.dto.LocationDTO;
import org.example.util.CryptUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CryptUtil cryptUtil;
//    private final OpenWeatherService openWeatherService = new OpenWeatherService();

    public User findByName(String username){
        return userRepository.findByName(username).orElse(null);
    }


    public User save(User user){
        String hashPassword = cryptUtil.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        return userRepository.save(user);
    }

    public void removeLocationFromUser(User user, Location location){
        userRepository.removeLocationFromUser(user,location);
    }
    public User addLocationToUser(User user, Location location){
        User user1 = userRepository.addLocationToUser(user,location);
        return user1;

    }



//    public List<LocationDTO> loadLocations(User user) {
//        List<LocationDTO> locationDTOList = new ArrayList<>();
//        for (Location location: user.getLocations()){
//            LocationDTO locationDTO = openWeatherService.getLocationByName(location.getName());
//            locationDTOList.add(locationDTO);
//        }
//        return locationDTOList;
//
//    }
}
