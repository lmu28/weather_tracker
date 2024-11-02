package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.LocationService;
import org.example.service.OpenWeatherService;
import org.example.service.UserService;
import org.example.service.dto.LocationDTO;
import org.example.util.AuthUtil;
import org.example.util.ContextUtil;
import org.example.util.CryptUtil;
import org.example.util.TemplateUtil;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/")
public class HomeServlet extends HttpServlet {

    public static final String AUTHENTICATION = "authentication";
    public static final String USER = "user";
    public static final String LOCATIONS = "locations";

    private final AuthUtil authUtil = new AuthUtil();

//    private final UserService userService = ;

    private final LocationService locationService = new LocationService(new OpenWeatherService(),new UserService(new UserRepositoryHibernate(),new CryptUtil()), new LocationRepositoryHibernate());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object auth = req.getAttribute(AUTHENTICATION);
        if (authUtil.isAuthenticated(auth)){
            User user = (User) req.getAttribute(USER);
           // List<LocationDTO> locationDTOList  = userService.loadLocations(user);
            List<LocationDTO> locationDTOList  = locationService.getCurrentTemps(user);
            req.setAttribute(LOCATIONS, locationDTOList);
        }


        Context context = ContextUtil.build(req);
        String html = TemplateUtil.template("/index.html", context);

        resp.setStatus(200);
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(html);



    }
}
