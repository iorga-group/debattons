package com.iorga.debattons.apiserver.api;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

@Path("/reaction")
public class ReactionResource {
  @Path("/test")
  @GET
  public String test() {
    return "test";
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public StreamingOutput create() {
    try {
      Graph graph = OrientGraph.open("remote:localhost/debattons", "api-server", "password");
//      Graph graph = new OrientGraphFactory("remote:localhost/debattons", "root", "zh7cfMPJMsW5")
//        .getNoTx().open();
      graph.tx().open();//.onReadWrite(Transaction.READ_WRITE_BEHAVIOR.AUTO);
      try {
        Vertex v = graph.addVertex(T.label, "Test", "testProperty", "value");
        graph.tx().commit();
        return outputStream -> graph.io(IoCore.graphson()).writer().create().writeVertex(outputStream, v);
      } catch (Exception e) {
        if (graph.tx().isOpen()) {
          graph.tx().rollback();
        }
        throw e;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
