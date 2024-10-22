package org.example.service;


import org.example.enums.ValidationError;
import org.example.model.User;
import org.example.service.dto.UserValidationDTO;
import org.example.util.CryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.enums.ValidationError.NOT_VALID_USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserCredentialsValidatorTest {


    public UserCredentialsValidator validator;

    @Mock
    public UserService userService;

    @Mock
    public User user;

    @Mock
    public CryptUtil cryptUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new UserCredentialsValidator(cryptUtil,userService);
    }

    @Test
    void validateRegister_ValidCredentials() {
        String username = "user100";
        String password = "1234u5678";
        String confirmPassword = "1234u5678";

        doReturn(null).when(userService).findByName(username);

        UserValidationDTO dto = validator.validateRegister(username,password,confirmPassword);

        assertThat(dto.getUser()).isNull();
        assertThat(dto.getErrors()).isEmpty();

        verify(userService, times(1)).findByName(username);
        verifyNoMoreInteractions(userService);
    }


    @Test
    void validateRegister_NotValidUsername() {
        String username = "usr";
        String password = "1234u5678";
        String confirmPassword = "1234u5678";

        UserValidationDTO dto = validator.validateRegister(username,password,confirmPassword);

        assertThat(dto.getUser()).isNull();
        assertThat(dto.getErrors()).isNotNull();
        assertThat(dto.getErrors()).contains(ValidationError.NOT_VALID_USERNAME.getMessage());

        verifyNoInteractions(userService);
    }

    @Test
    void validateRegister_NotValidPassword() {
        String username = "user100";
        String password = "12345678";
        String confirmPassword = "12345678";


        UserValidationDTO dto = validator.validateRegister(username,password,confirmPassword);

        assertThat(dto.getUser()).isNull();
        assertThat(dto.getErrors()).isNotNull();
        assertThat(dto.getErrors()).contains(ValidationError.NOT_VALID_PASSWORD.getMessage());

        verifyNoInteractions(userService);
    }

    @Test
    void validateRegister_PasswordsNotMatched() {
        String username = "user100";
        String password = "1234u5678";
        String confirmPassword = "1234U5678";


        UserValidationDTO dto = validator.validateRegister(username,password,confirmPassword);

        assertThat(dto.getUser()).isNull();
        assertThat(dto.getErrors()).isNotNull();
        assertThat(dto.getErrors()).contains(ValidationError.PASSWORDS_NOT_MATCHED.getMessage());

        verifyNoInteractions(userService);
    }


    @Test
    void validateRegister_UserAlreadyExists() {
        String username = "user100";
        String password = "1234u5678";
        String confirmPassword = "1234u5678";

        doReturn(user).when(userService).findByName(username);

        UserValidationDTO dto = validator.validateRegister(username,password,confirmPassword);

        assertThat(dto.getUser()).isEqualTo(user);
        assertThat(dto.getErrors()).isNotNull();
        assertThat(dto.getErrors()).contains(ValidationError.USER_ALREADY_EXISTS.getMessage());

        verify(userService, times(1)).findByName(username);
        verifyNoMoreInteractions(userService);
    }


    @Test
    void validateLogin_UserExists() {
        String username = "user100";
        String password = "1234u5678";

        doReturn(user).when(userService).findByName(username);
        doReturn(true).when(cryptUtil).checkPassword(eq(password), any());

        UserValidationDTO dto = validator.validateLogin(username,password);

        assertThat(dto.getUser()).isEqualTo(user);
        assertThat(dto.getErrors()).isEmpty();

        verify(userService, times(1)).findByName(username);
        verifyNoMoreInteractions(userService);


    }


    @Test
    void validateLogin_UserNotExists() {
        String username = "user100";
        String password = "1234u5678";

        doReturn(null).when(userService).findByName(username);

        UserValidationDTO dto = validator.validateLogin(username,password);

        assertThat(dto.getUser()).isEqualTo(null);
        assertThat(dto.getErrors().size()).isEqualTo(1);

        verify(userService, times(1)).findByName(username);
        verifyNoMoreInteractions(userService);


    }



    @Test
    void validateLogin_NotValidLogin() {
        String username = "100user";
        String password = "1234u5678";

        UserValidationDTO dto = validator.validateLogin(username,password);

        assertThat(dto.getUser()).isNull();
        assertThat(dto.getErrors().size()).isEqualTo(1);

        verifyNoInteractions(userService);


    }



}