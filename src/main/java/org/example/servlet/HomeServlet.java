package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.ContextUtil;
import org.example.util.TemplateUtil;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/")
public class HomeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = ContextUtil.build(req);
        String html = TemplateUtil.template("/index.html", context);

        resp.setStatus(200);
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(html);



    }
}
