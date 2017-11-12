package com.iorga.debattons.apiserver.util;

import com.iorga.debattons.apiserver.version.VersionService;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GraphUtils {
  private final ThreadLocal<Graph> inTransactionGraphThreadLocal = new ThreadLocal<>();

  private Object rootVertexId = null;

  @Autowired @Lazy
  private VersionService versionService;
  @Autowired
  private GraphManager graphManager;

  public interface InGraphTransactionHandler<T> {
    T doInGraphTransaction(Graph graph) throws Exception;
  }

  public interface InGraphVoidTransactionHandler {
    void doInGraphTransaction(Graph graph) throws Exception;
  }

  public interface GraphManager {
    Graph open();
    String vertexIdToString(Object vertexId);
    Object vertexIdFromString(String vertexId);
  }

  @Component
  public class GraphManagerImpl implements GraphManager {
    @Override
    public Graph open() {
      return OrientGraph.open("remote:localhost/debattons", "api-server", "password");
    }

    @Override
    public String vertexIdToString(Object vertexId) {
      return vertexId.toString();
    }

    @Override
    public Object vertexIdFromString(String vertexId) {
      return vertexId;
    }
  }


  public <T> T doInGraphTransaction(InGraphTransactionHandler<T> inGraphTransactionHandler) throws Exception {
    Graph graph = inTransactionGraphThreadLocal.get();
    if (graph != null) {
      // Already in a Transaction
      return inGraphTransactionHandler.doInGraphTransaction(graph);
    } else {
      try {
        graph = graphManager.open();
        boolean supportsTransactions = graph.features().graph().supportsTransactions();
//      Graph graph = new OrientGraphFactory("remote:localhost/debattons", "reaction-server", "password")
//        .getNoTx().open();
        if (supportsTransactions) {
          graph.tx().open();//.onReadWrite(Transaction.READ_WRITE_BEHAVIOR.AUTO);
        }
        inTransactionGraphThreadLocal.set(graph);
        try {
          T result = inGraphTransactionHandler.doInGraphTransaction(graph);
          if (supportsTransactions) {
            graph.tx().commit();
          }
          return result;
        } catch (Exception e) {
          if (supportsTransactions && graph.tx().isOpen()) {
            graph.tx().rollback();
          }
          throw e;
        } finally {
          inTransactionGraphThreadLocal.remove();
        }
      } catch (Exception e) {
        e.printStackTrace(); // TODO handle exception better
        throw e;
      }
    }
  }

  public void doInGraphTransaction(InGraphVoidTransactionHandler inGraphVoidTransactionHandler) throws Exception {
    doInGraphTransaction(graph -> {
      inGraphVoidTransactionHandler.doInGraphTransaction(graph);
      return null;
    });
  }

  public Vertex getRoot(Graph graph) {
    return getRootTraversal(graph).next();
  }

  public GraphTraversal<Vertex, Vertex> getRootTraversal(Graph graph) {
    if (rootVertexId != null) {
      return graph.traversal().V(rootVertexId);
    } else {
      GraphTraversal<Vertex, Vertex> rootTraversal = graph.traversal().V().hasLabel("Root");
      if (rootTraversal.hasNext()) {
        rootVertexId = rootTraversal.next().id();
        return getRootTraversal(graph);
      } else {
        return rootTraversal;
      }
    }
  }

  public String getStringVertexId(Vertex vertex, Graph graph) throws IOException {
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    graph.io(IoCore.graphson()).writer().create().writeObject(outputStream, vertex.id());
//    return outputStream.toString();
    return graphManager.vertexIdToString(vertex.id());
  }

  public Object getObjectVertexId(String vertexId, Graph graph) {
    return graphManager.vertexIdFromString(vertexId);
  }

//  public static Object jsonVertexIdToGraphObject(String jsonId, Graph graph) throws IOException {
//    return graph.io(IoCore.graphson()).reader().create().readObject(new ByteArrayInputStream(jsonId.getBytes()), Object.class);
//  }
}
