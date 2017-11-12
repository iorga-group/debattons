package com.iorga.debattons.apiserver.reaction;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.IOException;

public class Reaction {
  private String id;
  private String title;
  private String content;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public static Reaction fromVertex(Vertex vertex, GraphUtils graphUtils) throws IOException {
    Reaction reaction = new Reaction();
    reaction.setId(graphUtils.getStringVertexId(vertex, vertex.graph()));
    reaction.setTitle(vertex.value("title"));
    reaction.setContent(vertex.value("content"));
    return reaction;
  }
}
