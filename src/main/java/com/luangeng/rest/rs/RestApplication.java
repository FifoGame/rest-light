package com.luangeng.rest.rs;

import com.luangeng.rest.ws.Hello;

import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RestApplication extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Hello.class);
        return classes;
    }

    public Set<Object> getSingletons() {
        return super.getSingletons();
    }

    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }


}
