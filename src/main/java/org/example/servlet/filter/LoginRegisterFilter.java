package org.example.servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/login", "/register"})
public class LoginRegisterFilter implements Filter {
    public static final String AUTHENTICATION = "authentication";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       HttpServletRequest httpServletRequest =  ((HttpServletRequest)servletRequest);
       HttpServletResponse httpServletResponse =  ((HttpServletResponse)servletResponse);


       Boolean isAuth = (Boolean) httpServletRequest.getAttribute(AUTHENTICATION);
       if (isAuth != null && isAuth){
         httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
       }else {
           filterChain.doFilter(servletRequest,servletResponse);
       }

    }
}
