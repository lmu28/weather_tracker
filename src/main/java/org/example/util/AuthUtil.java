package org.example.util;

public class AuthUtil {

    public boolean isAuthenticated(Object auth){
        return auth != null && auth instanceof Boolean && (Boolean)auth;

    }
}
