package com.iorga.debattons.apiserver.reaction;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ReactionService {

  @Autowired
  private GraphUtils graphUtils;


  public Reaction create(Reaction reaction) throws Exception {
    return graphUtils.doInGraphTransaction(graph -> {
      Vertex reactionVertex = graph.addVertex(
        T.label, "Reaction",
        "title", reaction.getTitle(),
        "content", reaction.getContent(),
        "creationDate", new Date());
      graphUtils.getRoot(graph).addEdge("created", reactionVertex);
      reaction.setId(graphUtils.getStringVertexId(reactionVertex, graph));
      return reaction;
    });
  }

  public List<Reaction> findRoots() throws Exception {
    return graphUtils.doInGraphTransaction(graph -> {
      return graphUtils.getRootTraversal(graph)
        .out("created")
        .order().by("creationDate", Order.decr)
        .map(vertexTraverser -> {
          try {
            return Reaction.fromVertex(vertexTraverser.get(), graphUtils);
          } catch (IOException e) {
            throw new RuntimeException("Problem while creating the Reaction from Vertex", e);
          }
        }).toList();
    });
  }

  public Reaction findById(final String id) throws Exception {
    return graphUtils.doInGraphTransaction(graph -> {
      return Reaction.fromVertex(graph.traversal().V(graphUtils.getObjectVertexId(id, graph)).next(), graphUtils);
    });
  }
}
