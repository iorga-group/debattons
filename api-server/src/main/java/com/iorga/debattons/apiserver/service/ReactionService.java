package com.iorga.debattons.apiserver.service;

import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class ReactionService {

  public Reaction create(Reaction reaction) throws Exception {
    return GraphUtils.doInGraphTransaction(graph -> {
      Vertex reactionVertex = graph.addVertex(
        T.label, "Reaction",
        "title", reaction.getTitle(),
        "content", reaction.getContent(),
        "creationDate", new Date());
      GraphUtils.getRoot(graph).addEdge("created", reactionVertex);
      reaction.setId(GraphUtils.getStringVertexId(reactionVertex, graph));
      return reaction;
    });
  }

  public List<Reaction> findRoots() throws Exception {
    return GraphUtils.doInGraphTransaction(graph -> {
      return GraphUtils.getRootTraversal(graph)
        .out("created")
        .order().by("creationDate", Order.decr)
        .map(vertexTraverser -> {
          try {
            return Reaction.fromVertex(vertexTraverser.get());
          } catch (IOException e) {
            throw new RuntimeException("Problem while creating the Reaction from Vertex", e);
          }
        }).toList();
    });
  }

  public Reaction findWithId(final String id) throws Exception {
    return GraphUtils.doInGraphTransaction(graph -> {
      return Reaction.fromVertex(graph.traversal().V(GraphUtils.getObjectVertexId(id, graph)).next());
    });
  }
}
