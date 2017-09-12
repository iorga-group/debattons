package com.iorga.debattons.apiserver.util;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;

public class GraphUtils {

  public interface InGraphTransactionHandler<T> {
    T doInGraphTransaction(Graph graph) throws Exception;
  }

  public static <T> T doInGraphTransaction(InGraphTransactionHandler<T> inGraphTransactionHandler) throws Exception {
    try {
      Graph graph = OrientGraph.open("remote:localhost/debattons", "api-server", "password");
//      Graph graph = new OrientGraphFactory("remote:localhost/debattons", "api-server", "password")
//        .getNoTx().open();
      graph.tx().open();//.onReadWrite(Transaction.READ_WRITE_BEHAVIOR.AUTO);
      try {
        T result = inGraphTransactionHandler.doInGraphTransaction(graph);
        graph.tx().commit();
        return result;
      } catch (Exception e) {
        if (graph.tx().isOpen()) {
          graph.tx().rollback();
        }
        throw e;
      }
    } catch (Exception e) {
      e.printStackTrace(); // TODO handle exception better
      throw e;
    }
  }
}
