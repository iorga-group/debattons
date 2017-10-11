package com.iorga.debattons.apiserver.api;

import com.iorga.debattons.apiserver.Main;
import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ReactionResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    // set the GraphManager to be in memory & bootstrap the graph
    TinkerGraph graph = TinkerGraph.open();
    GraphUtils.setGraphManager(new GraphUtils.GraphManager() {
      @Override
      public Graph open() {
        return graph;
      }

      @Override
      public String vertexIdToString(Object vertexId) {
        return vertexId.toString();
      }

      @Override
      public Object vertexIdFromString(String vertexId) {
        return Long.parseLong(vertexId);
      }
    });

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
    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction);
    assertThat(reactionOut.getTitle()).isEqualTo(reaction.getTitle());
    assertThat(reactionOut.getContent()).isEqualTo(reaction.getContent());
    assertThat(reactionOut.getId()).isNotEmpty();
  }

  private Reaction createReaction(Reaction reaction) {
    return target.path("/reaction/").request().post(Entity.entity(reaction, MediaType.APPLICATION_JSON), Reaction.class);
  }

  private Reaction newTestReaction() {
    Reaction reaction = new Reaction();
    reaction.setTitle("Test Title");
    reaction.setContent("Test content lorem ipsum");
    return reaction;
  }

  @Test
  public void findRootsTest() {
    Reaction reaction = newTestReaction();
    createReaction(reaction);
    reaction.setTitle("Test title 2");
    createReaction(reaction);
    List<Reaction> reactions = target.path("/reaction/roots").request().get(new GenericType<List<Reaction>>(){});
    assertThat(reactions).hasSize(2);
    assertThat(reactions.get(0).getTitle()).isEqualTo(reaction.getTitle()); // Latest reaction must be the first one to be listed
  }

  @Test
  public void findWithIdTest() {
    Reaction reaction = newTestReaction();
    Reaction createdReaction = createReaction(reaction);
    Reaction foundReaction = target.path("/reaction/"+createdReaction.getId()).request().get(Reaction.class);
    assertThat(foundReaction.getTitle()).isEqualTo(reaction.getTitle());
  }
}
