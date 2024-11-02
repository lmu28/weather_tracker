package org.example.repository;

import org.example.model.Location;

import java.util.Optional;

public interface LocationRepository {

//    int deleteByUser(User user);

    void save(Location location);

    Optional<Location> findByName(String name);

    void remove(Location location);


}
