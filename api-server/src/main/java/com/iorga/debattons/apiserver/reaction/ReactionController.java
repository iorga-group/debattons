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
  public Reaction post(@RequestBody Reaction reaction) throws Exception {
    return reactionService.create(reaction);
  }

  @GetMapping("/roots")
  public Collection<Reaction> getRoots() throws Exception {
    return reactionService.findRoots();
  }

  @GetMapping("/{id}")
  public Reaction getById(@PathVariable("id") String id) throws Exception {
    return reactionService.findById(id);
  }
}
