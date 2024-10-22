package org.example.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;
import org.example.repository.SessionRepositoryHibernate;
import org.example.service.CookieService;
import org.example.service.SessionService;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {


    public static final String SESSION_ID = "SESSION_ID";
    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());
    private final CookieService cookieService = new CookieService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = (Session)req.getAttribute("session");
        sessionService.deleteById(session.getId());
        cookieService.deleteCookie(SESSION_ID, resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
