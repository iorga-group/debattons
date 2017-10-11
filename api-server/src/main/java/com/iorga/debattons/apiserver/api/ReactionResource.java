package com.iorga.debattons.apiserver.api;

import com.iorga.debattons.apiserver.entity.Reaction;
import com.iorga.debattons.apiserver.service.ReactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

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

  @Path("/roots")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Reaction> findRoots() throws Exception {
    return reactionService.findRoots();
  }

  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Reaction findWithId(@PathParam("id") String id) throws Exception {
    return reactionService.findWithId(id);
  }
}
