package com.luangeng.rest.servlet;

import com.luangeng.rest.impl.ServletCfg;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet
public class RestServlet implements Servlet {

    private ServletCfg cfg;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.cfg = (ServletCfg) config;
    }

    @Override
    public ServletConfig getServletConfig() {
        return cfg;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "REST Servlet";
    }

    @Override
    public void destroy() {

    }

}
