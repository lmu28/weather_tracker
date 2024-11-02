package org.example.servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.AuthUtil;

import java.io.IOException;

@WebFilter(urlPatterns = {"/login", "/register"})
public class LoginRegisterFilter implements Filter {
    public static final String AUTHENTICATION = "authentication";

    private final AuthUtil authUtil = new AuthUtil();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       HttpServletRequest httpServletRequest =  ((HttpServletRequest)servletRequest);
       HttpServletResponse httpServletResponse =  ((HttpServletResponse)servletResponse);


       Object auth =  httpServletRequest.getAttribute(AUTHENTICATION);
       if (authUtil.isAuthenticated(auth)){
         httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
       }else {
           filterChain.doFilter(servletRequest,servletResponse);
       }

    }
}
