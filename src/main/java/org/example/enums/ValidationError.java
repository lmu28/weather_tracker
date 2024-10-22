package org.example.enums;

public enum ValidationError {
    USER_ALREADY_EXISTS("User with this username already exists."),
    USER_NOT_EXISTS("User with this username or password not exists."),
    NOT_VALID_USERNAME("Username must start with letter and be between 5 and 20 characters long and contain only letters and numbers."),
    NOT_VALID_PASSWORD("Password must be at between 8 and 32 characters long contain only letters and numbers and contain at least one letter and one number."),
    PASSWORDS_NOT_MATCHED("Passwords don't match");


    private String message;

    ValidationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
