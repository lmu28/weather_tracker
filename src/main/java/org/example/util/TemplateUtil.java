package org.example.util;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.helpers.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;


public class TemplateUtil {

    private static TemplateEngine te = new TemplateEngine();
    static {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("utf-8");
        resolver.setPrefix("/templates");
        te.setTemplateResolver(resolver);
    }



    public static String template(String file, Context context) throws IOException {
        String html =  te.process(file,context);
        return html;
    }
}
