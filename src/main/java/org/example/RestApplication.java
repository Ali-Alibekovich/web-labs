package org.example;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/web")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("org.example.service");

        register(JacksonFeature.class);
    }
}