package com.luangeng;

import com.luangeng.rest.server.ServletServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new ServletServer().run(port);
    }
}
