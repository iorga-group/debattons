package com.iorga.debattons.apiserver.api;

import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.service.ReactionService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/reaction")
public class ReactionResource {

  private ReactionService reactionService = new ReactionService();

  @Path("/test")
  @GET
  public String test() {
    return "test";
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Reaction create(Reaction reaction) throws Exception {
    return reactionService.create(reaction);
  }
}
