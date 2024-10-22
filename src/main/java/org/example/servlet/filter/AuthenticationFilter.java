package org.example.servlet.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;
import org.example.repository.SessionRepositoryHibernate;
import org.example.service.CookieService;
import org.example.service.SessionService;
import org.example.service.UserService;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    public static final String SESSION_ID = "SESSION_ID";
    public static final String AUTHENTICATION = "authentication";
    public static final String SESSION = "session";
    private final CookieService cookieService = new CookieService();
    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Cookie cookie = cookieService.getCookie(SESSION_ID, httpRequest);

        httpRequest.setAttribute(AUTHENTICATION, false);
        if (cookie != null){
            Session session = sessionService.get(cookie.getValue());
            if (sessionService.isValid(session)){
                httpRequest.setAttribute(SESSION, session);
                httpRequest.setAttribute(AUTHENTICATION, true);
                httpRequest.setAttribute("username", session.getUser().getName());
            }else {
                cookieService.deleteCookie(cookie.getName(),httpResponse);
            }
        }


        filterChain.doFilter(servletRequest,servletResponse);
    }
}
