package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryHibernate;
import org.example.util.CryptUtil;
import org.mindrot.jbcrypt.BCrypt;


@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CryptUtil cryptUtil;

    public User findByName(String username){
        return userRepository.findByName(username).orElse(null);
    }


    public User save(User user){
        String hashPassword = cryptUtil.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        return userRepository.save(user);
    }
}
