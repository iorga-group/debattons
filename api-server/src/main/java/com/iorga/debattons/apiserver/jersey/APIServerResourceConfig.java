package com.iorga.debattons.apiserver.jersey;

import com.iorga.debattons.apiserver.Main;
import com.iorga.debattons.apiserver.service.VersionService;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class APIServerResourceConfig extends ResourceConfig {

    public APIServerResourceConfig() throws Exception {
      register(CORSFilter.class);
      register(LoggingFeature.class);
      packages(Main.class.getPackage().getName());
      new VersionService().bootstrap();
    }

}
