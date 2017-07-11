package com.iorga.debattons.apiserver.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/reaction")
public class ReactionResource {
  @Path("/test")
  @GET
  public String test() {
    return "test";
  }
}
