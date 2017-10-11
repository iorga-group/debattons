package com.iorga.debattons.apiserver;

import com.iorga.debattons.apiserver.jersey.APIServerResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8080/api/";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() throws Exception {
    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new APIServerResourceConfig());
  }

  /**
   * Main method.
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws Exception {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
      + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.stop();
  }
}
