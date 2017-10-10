package com.iorga.debattons.apiserver.service;

import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;

import java.io.ByteArrayOutputStream;


public class ReactionService {

  public Reaction create(Reaction reaction) throws Exception {
    return GraphUtils.doInGraphTransaction(graph -> {
      Vertex v = graph.addVertex(
        T.label, "Reaction",
        "title", reaction.getTitle(),
        "content", reaction.getContent());
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      graph.io(IoCore.graphson()).writer().create().writeObject(outputStream, v.id());
      reaction.setId(outputStream.toString());
      return reaction;
    });
  }

}
