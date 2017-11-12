package com.iorga.debattons.apiserver.version;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class VersionService {
  private final static Logger LOG = LoggerFactory.getLogger(VersionService.class);


  @Autowired
  private GraphUtils graphUtils;


  @PostConstruct
  public void bootstrap() throws Exception {
    graphUtils.doInGraphTransaction(graph -> {
      GraphTraversal<Vertex, Vertex> rootTraversal = graphUtils.getRootTraversal(graph);
      Vertex rootVertex;
      if (rootTraversal.hasNext()) {
        rootVertex = rootTraversal.next();
      } else {
        rootVertex = createRootVertex(graph);
      }
//      if ("0.0.0".equals(rootVertex.property("version").value())) {
//        updateToV0_0_1(graph, rootVertex);
//      }
      LOG.info("Débattons v{} has bootstrapped.", rootVertex.property("version").value());
    });
  }

  private Vertex createRootVertex(Graph graph) {
    return graph.addVertex(
      T.label, "Root",
      "version", "0.0.0"
    );
  }

}
