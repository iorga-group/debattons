package com.iorga.debattons.apiserver.test;

import com.iorga.debattons.apiserver.ApiServerApplication;
import com.iorga.debattons.apiserver.util.GraphUtils;
import com.iorga.debattons.apiserver.version.VersionService;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractControllerTest {
  @Autowired
  protected TestRestTemplate restTemplate;

  @Autowired
  protected VersionService versionService;

  protected static TinkerGraph graph = TinkerGraph.open();

  @Configuration
  @Import(ApiServerApplication.class)
  static class Config {
    @Bean
    @Primary
    public GraphUtils.GraphManager createGraphManager() {
      return new GraphUtils.GraphManager() {
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
      };
    }
  }

  @Before
  public void setUp() throws Exception {
    // set the GraphManager to be in memory & bootstrap the graph
    graph = TinkerGraph.open();
    versionService.bootstrap();
  }

  @After
  public void tearDown() {
    graph.close();
  }
}
