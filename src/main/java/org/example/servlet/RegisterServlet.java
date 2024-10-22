package org.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.CookieService;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final CookieService cookieService = new CookieService();
    private final UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());

    private final SessionService sessionService = new SessionService(new SessionRepositoryHibernate());
    private final UserCredentialsValidator validator = new UserCredentialsValidator(new CryptUtil(), userService);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        List<String> errors = (List<String>) httpSession.getAttribute("errors");
        if (errors != null) {
            req.setAttribute("errors", errors);
            httpSession.removeAttribute("errors");
        }
        Context context = ContextUtil.build(req);
        String html = TemplateUtil.template("/register.html", context);
        resp.getWriter().println(html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        UserValidationDTO userDTO = validator.validateRegister(username, password, confirmPassword);
        if (!userDTO.getErrors().isEmpty()) {
            req.getSession().setAttribute("errors", userDTO.getErrors());
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        User transientUser = new User(0,username, password, null);
        User persistUser = userService.save(transientUser);
        if (persistUser == null) {
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);
            return;
        }
        Session session = sessionService.save(persistUser);
        cookieService.setCookie("SESSION_ID", session.getId(),resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}


