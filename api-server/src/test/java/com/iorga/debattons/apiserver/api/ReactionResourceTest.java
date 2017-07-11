package com.iorga.debattons.apiserver.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.iorga.debattons.apiserver.Main;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReactionResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client client = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Main.startServer())
    // --
    // client.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

    target = client.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.stop();
  }

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  @Test
  public void testTest() {
    String responseMsg = target.path("/reaction/test").request().get(String.class);
    assertEquals("test", responseMsg);
  }
}
