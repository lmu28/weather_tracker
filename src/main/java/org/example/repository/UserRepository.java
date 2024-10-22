package org.example.repository;

import org.example.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByNamePassword (String name, String password);


    Optional<User> findByName(String username);

    User save(User user);
}
