package com.iorga.debattons.apiserver.api;

import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;

@Path("/reaction")
public class ReactionResource {
  @Path("/test")
  @GET
  public String test() {
    return "test";
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
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
