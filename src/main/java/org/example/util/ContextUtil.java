package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.thymeleaf.context.Context;

import java.util.Enumeration;

public class ContextUtil {



    public static Context build(HttpServletRequest request){
        Context context = new Context();
        Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()){
            String a = attributes.nextElement();
            context.setVariable(a, request.getAttribute(a));
        }
        context.setVariable("applicationContext", request.getContextPath());
        return context;
    }


}
