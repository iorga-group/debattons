package com.iorga.debattons.apiserver.reaction;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;

public class Reaction {
  private String id;
  private String title;

  @NotNull
  private String content;

  private Set<Reaction> reactedTo;

  private Reaction reactedFrom;

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

  public Set<Reaction> getReactedTo() {
    return reactedTo;
  }

  public void setReactedTo(Set<Reaction> reacted) {
    this.reactedTo = reacted;
  }

  public Reaction getReactedFrom() {
    return reactedFrom;
  }

  public void setReactedFrom(Reaction reactedFrom) {
    this.reactedFrom = reactedFrom;
  }

  public static Reaction fromVertex(Vertex vertex, GraphUtils graphUtils) throws IOException {
    Reaction reaction = new Reaction();
    reaction.setId(graphUtils.getStringVertexId(vertex, vertex.graph()));
    reaction.setTitle(vertex.value("title"));
    reaction.setContent(vertex.value("content"));
    return reaction;
  }
}
