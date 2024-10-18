package org.example.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.model.Session;

import java.sql.Timestamp;

public class CookieService {
    public Cookie getCookie(String name, HttpServletRequest httpRequest) {
        Cookie cookie  = null;
        for (Cookie c:  httpRequest.getCookies()){
            if (c.getName().equals("name")){
                 cookie = c;
                 break;
            }
        }
        return cookie;
    }
}
