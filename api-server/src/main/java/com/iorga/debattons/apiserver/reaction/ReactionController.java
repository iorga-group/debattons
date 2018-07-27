package com.iorga.debattons.apiserver.reaction;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/reactions")
public class ReactionController {

  private ReactionService reactionService;


  public ReactionController(ReactionService reactionService) {
    this.reactionService = reactionService;
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public Reaction post(
    @RequestBody Reaction reaction,
    @RequestParam(value = "reactToReactionId", required = false) String reactToReactionId,
    @RequestParam(value = "reactionType", required = false) String reactionType
  ) throws Exception {
    String login = SecurityContextHolder.getContext().getAuthentication().getName();
    if (reactToReactionId != null) {
      return reactionService.createByReactionReactingToReactionId(reaction, reactToReactionId, reactionType, login);
    } else {
      return reactionService.create(reaction, login);
    }
  }

  @GetMapping("/roots")
  public Collection<Reaction> getRoots() throws Exception {
    return reactionService.findRoots();
  }

  @GetMapping("/{id}")
  public Reaction getByIdLoadingReactedToDepth(@PathVariable("id") String id, @RequestParam(value = "reactedToDepth", required = false, defaultValue = "0") int reactedDepth) throws Exception {
    return reactionService.findByIdLoadingReactedToDepth(id, reactedDepth);
  }

  @PostMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public void getByIdLoadingReactedToDepth(
    @PathVariable("id") String id,
    @RequestParam("reactionType") String reactionType
  ) throws Exception {
    String login = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("agree".equals(reactionType)) {
      reactionService.agreeWithById(id, login);
    } else {
      throw new IllegalArgumentException("Could not create a link of type '"+reactionType+"', only 'agree' is allowed.");
    }
  }
}
