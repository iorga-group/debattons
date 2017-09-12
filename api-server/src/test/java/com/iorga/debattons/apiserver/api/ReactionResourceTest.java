package com.iorga.debattons.apiserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorga.debattons.apiserver.Main;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;

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

  @Test
  public void createTest() throws IOException {
    String responseMsg = target.path("/reaction/").request().post(null, String.class);
    Map<String, Object> jsonResponseMap = new ObjectMapper().readValue(responseMsg, Map.class);
    assertThat(jsonResponseMap).contains(ImmutablePair.of("label", "Test"));
    Map<String, Object> properties = (Map<String, Object>) jsonResponseMap.get("properties");
    assertThat(properties).containsKey("testProperty");
    //assertThat(responseMsg).matches(Pattern.compile("\\{\"id\":\\{\"clusterId\":\\d+,\"clusterPosition\":\\d+\\},\"label\":\"Test\",\"properties\":\\{\"testProperty\":\\{\\{\"id\":\\{\"clusterId\":\\d+,\"clusterPosition\":\\d+\\},\"value\":\"value\"\\}\\]\\}\\}"));
  }
}
