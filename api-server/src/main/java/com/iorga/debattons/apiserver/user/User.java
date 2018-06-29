package com.iorga.debattons.apiserver.user;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class User {
  private String id;
  @NotNull
  private String login;
  @NotNull
  private String password;
  private byte[] passwordHash;
  private byte[] salt;
  private String email;

  public static User fromVertex(Vertex vertex, GraphUtils graphUtils) throws IOException {
    User user = new User();
    user.setId(graphUtils.getStringVertexId(vertex, vertex.graph()));
    user.setLogin(vertex.value("login"));
    user.setPasswordHash(vertex.value("passwordHash"));
    user.setSalt(vertex.value("salt"));
    user.setEmail(vertex.value("email"));
    return user;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public byte[] getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(byte[] passwordHash) {
    this.passwordHash = passwordHash;
  }

  public byte[] getSalt() {
    return salt;
  }

  public void setSalt(byte[] salt) {
    this.salt = salt;
  }
}
