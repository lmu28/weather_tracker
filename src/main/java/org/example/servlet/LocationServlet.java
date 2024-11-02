package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Location;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.LocationRepositoryHibernate;
import org.example.repository.UserRepositoryHibernate;
import org.example.service.LocationService;
import org.example.service.OpenWeatherService;
import org.example.service.UserService;
import org.example.util.AuthUtil;
import org.example.util.ContextUtil;
import org.example.util.CryptUtil;
import org.example.util.TemplateUtil;
import org.thymeleaf.context.Context;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/location")
public class LocationServlet extends HttpServlet {

    public static final String METHOD = "_method";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PUT = "PUT";

    public static final String AUTHENTICATION = "authentication";
    public static final String SESSION = "session";
    public static final String USER = "user";
    public static final String LOCATION_NAME = "locationName";

    private final AuthUtil authUtil = new AuthUtil();
    private final UserService userService = new UserService(new UserRepositoryHibernate(), new CryptUtil());

    private final OpenWeatherService openWeatherService = new OpenWeatherService();
    private final LocationService locationService = new LocationService(openWeatherService, userService, new LocationRepositoryHibernate());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object auth = req.getAttribute(AUTHENTICATION);
        if (!authUtil.isAuthenticated(auth)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String name = req.getParameter("locationName");
        Location location = locationService.searchLocation(name);
        if (location != null) {

            req.setAttribute("locations", List.of(location));
        }

        Context context = ContextUtil.build(req);


        String html = TemplateUtil.template("/search.html", context);
        resp.setContentType("text/html");
        resp.setStatus(200);
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(html);


//        resp.sendRedirect(req.getContextPath() + "/");


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter(METHOD);

        switch (method) {
            case METHOD_DELETE -> doDelete(req, resp);
            case METHOD_PUT -> doPut(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object auth = req.getAttribute(AUTHENTICATION);
        if (!authUtil.isAuthenticated(auth)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String locationName = req.getParameter(LOCATION_NAME);
        User user = ((Session) req.getAttribute(SESSION)).getUser();

        userService.removeLocationFromUser(user, new Location(locationName, 0, 0)); // compares by name
        resp.sendRedirect(req.getContextPath() + "/");


    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("======================PUT=======================");
        String name = req.getParameter("locationName");
        Location location = locationService.findByName(name);
        if (location == null){
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);
        }
        User user =(User) req.getAttribute(USER);
        user = userService.addLocationToUser(user,location);
        if (!user.getLocations().contains(location)){
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
