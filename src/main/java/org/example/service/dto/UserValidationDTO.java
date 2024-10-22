package org.example.service.dto;

import lombok.Getter;
import org.example.model.User;

import java.util.List;


@Getter
public class UserValidationDTO {

    private User user;
    private List<String> errors;

    public UserValidationDTO(User user, List<String> errors) {
        this.user = user;
        this.errors = errors;
    }


}
