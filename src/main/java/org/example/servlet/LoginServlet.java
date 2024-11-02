package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Session;
import org.example.repository.SessionRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.util.CookieUtil;
import org.example.service.SessionService;
import org.example.service.UserCredentialsValidator;
import org.example.service.UserService;
import org.example.service.dto.UserValidationDTO;
import org.example.util.ContextUtil;
import org.example.util.CryptUtil;
import org.example.util.TemplateUtil;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());

    private final CookieUtil cookieUtil = new CookieUtil();
    private final UserCredentialsValidator validator = new UserCredentialsValidator(
            new CryptUtil(),
            new UserService(new UserRepositoryHibernate(), new CryptUtil())
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        List<String> errors = (List<String>) httpSession.getAttribute("errors");
        if (errors != null) {
            req.setAttribute("errors", errors);
            httpSession.removeAttribute("errors");
        }

        Context context = ContextUtil.build(req);
        String html = TemplateUtil.template("/login.html", context);
        resp.getWriter().println(html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserValidationDTO userDTO = validator.validateLogin(username, password);
        if (!userDTO.getErrors().isEmpty()) {
            req.getSession().setAttribute("errors", userDTO.getErrors());
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Session session = sessionService.save(userDTO.getUser());
        cookieUtil.setCookie("SESSION_ID", session.getId(),resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
