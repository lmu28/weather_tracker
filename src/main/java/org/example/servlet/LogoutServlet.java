package org.example.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Session;
import org.example.repository.SessionRepositoryHibernate;
import org.example.util.AuthUtil;
import org.example.util.CookieUtil;
import org.example.service.SessionService;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {


    public static final String SESSION_ID = "SESSION_ID";

    public static final String AUTHENTICATION = "authentication";

    private final AuthUtil authUtil = new AuthUtil();

    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());
    private final CookieUtil cookieUtil = new CookieUtil();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object auth = req.getAttribute(AUTHENTICATION);
        if (!authUtil.isAuthenticated(auth)){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        Session session = (Session)req.getAttribute("session");
        sessionService.deleteById(session.getId());
        cookieUtil.deleteCookie(SESSION_ID, resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }

}
