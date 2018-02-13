package com.iorga.debattons.apiserver.user;

import org.springframework.data.gremlin.annotation.*;

import javax.validation.constraints.NotNull;

@Vertex
public class User {
  @Id
  private String id;
  @NotNull
  private String login;
  @NotNull
  private String password;
  private String email;


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
}
