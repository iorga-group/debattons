package com.iorga.debattons.apiserver.jersey;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * @author iORGA Group
 */
@ApplicationPath("/")
public class APIServerResourceConfig extends ResourceConfig {

    public APIServerResourceConfig() {
      register(CORSFilter.class);
//        register(LoggingFilter.class);
    }

}
