package org.example.servlet.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;
import org.example.repository.SessionRepositoryHibernate;
import org.example.util.CookieUtil;
import org.example.service.SessionService;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    public static final String SESSION_ID = "SESSION_ID";
    public static final String AUTHENTICATION = "authentication";
    public static final String SESSION = "session";
    public static final String USER = "user";
    private final CookieUtil cookieUtil = new CookieUtil();
    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Cookie cookie = cookieUtil.getCookie(SESSION_ID, httpRequest);

        httpRequest.setAttribute(AUTHENTICATION, false);
        if (cookie != null){
            Session session = sessionService.get(cookie.getValue());
            if (sessionService.isValid(session)){
                httpRequest.setAttribute(SESSION, session);
                httpRequest.setAttribute(AUTHENTICATION, true);
                httpRequest.setAttribute(USER, session.getUser());
            }else {
                cookieUtil.deleteCookie(cookie.getName(),httpResponse);
            }
        }


        filterChain.doFilter(servletRequest,servletResponse);


    }
}
