package com.iorga.debattons.apiserver.reaction;

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
  public Reaction post(
    @RequestBody Reaction reaction,
    @RequestParam(value = "reactToReactionId", required = false) String reactToReactionId,
    @RequestParam(value = "reactionType", required = false) String reactionType
  ) throws Exception {
    if (reactToReactionId != null) {
      return reactionService.createByReactionReactingToReactionId(reaction, reactToReactionId, reactionType);
    } else {
      return reactionService.create(reaction);
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
  public void getByIdLoadingReactedToDepth(
    @PathVariable("id") String id,
    @RequestParam("reactionType") String reactionType
  ) throws Exception {
    if ("agree".equals(reactionType)) {
      reactionService.agreeWithById(id);
    } else {
      throw new IllegalArgumentException("Could not create a link of type '"+reactionType+"', only 'agree' is allowed.");
    }
  }
}
