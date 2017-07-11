package com.iorga.debattons.apiserver;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author iORGA Group
 */
@ApplicationPath("/")
public class APIServerResourceConfig extends ResourceConfig {

    public APIServerResourceConfig() {
//        register(LoggingFilter.class);
    }

}
