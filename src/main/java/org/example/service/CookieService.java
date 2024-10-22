package org.example.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;

import java.sql.Timestamp;

public class CookieService {
    public Cookie getCookie(String name, HttpServletRequest httpRequest) {
        Cookie cookie  = null;
        for (Cookie c:  httpRequest.getCookies()){
            if (c.getName().equals(name)){
                 cookie = c;
                 break;
            }
        }
        return cookie;
    }

    public void setCookie(String name, String value, HttpServletResponse rs){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        rs.addCookie(cookie);
    }

    public void deleteCookie(String name, HttpServletResponse rs){
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        rs.addCookie(cookie);
    }
}
