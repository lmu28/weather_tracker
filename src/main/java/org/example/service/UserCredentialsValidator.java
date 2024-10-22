package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.dto.UserValidationDTO;
import org.example.util.CryptUtil;

import static org.example.enums.ValidationError.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class UserCredentialsValidator {

    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9]{4,19}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,32}$";

    private final CryptUtil cryptUtil;
    private final UserService userService;


    public UserValidationDTO validateRegister(String username, String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        if (!isValidLogin(username)) {
            errors.add(NOT_VALID_USERNAME.getMessage());
        }
        if (!isValidPassword(password)) {
            errors.add(NOT_VALID_PASSWORD.getMessage());
        }

        if (!isPasswordsMatched(password, confirmPassword)) {
            errors.add(PASSWORDS_NOT_MATCHED.getMessage());
        }

        if (!errors.isEmpty()) {
            return new UserValidationDTO(null, errors);
        }

        User user = userService.findByName(username);

        if (user != null) {
            errors.add(USER_ALREADY_EXISTS.getMessage());
        }

        return new UserValidationDTO(user, errors);

    }

    public UserValidationDTO validateLogin(String username, String password) {
        List<String> errors = new ArrayList<>();
        if (!isValidLogin(username)) {
            errors.add(USER_NOT_EXISTS.getMessage());
            return new UserValidationDTO(null, errors);
        }

        User user = userService.findByName(username);
        if (user == null) {
            errors.add(USER_NOT_EXISTS.getMessage());
            return new UserValidationDTO(null, errors);
        }
        String hash = user.getPassword();
        if (!cryptUtil.checkPassword(password, hash)) {
            errors.add(USER_NOT_EXISTS.getMessage());
            return new UserValidationDTO(null, errors);
        }

        return new UserValidationDTO(user, errors);
    }


    private boolean isValidLogin(String username) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    private boolean isPasswordsMatched(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
