package com.luangeng.servlet.server;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LG on 2017/12/4.
 */
public class ServletMng {

    private static Map<String, String> servletMap = new HashMap<>();

    static {
        servletMap.put("TestServlet".toLowerCase(), "com.luangeng.servlet.servlet.TestServlet");
        servletMap.put("TestHttpServlet".toLowerCase(), "com.luangeng.servlet.servlet.TestHttpServlet");
    }

    public static Servlet getServlet(String uri) {
        try {
            int n = uri.indexOf(".do");
            if (n == -1) return null;
            String str = uri.substring(0, n);
            str = str.substring(str.lastIndexOf("/") + 1);
            String classname = servletMap.get(str.toLowerCase());
            Class clazz = Class.forName(classname);
            return (Servlet) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void destroy(Servlet s) {
        s.destroy();
    }
}
