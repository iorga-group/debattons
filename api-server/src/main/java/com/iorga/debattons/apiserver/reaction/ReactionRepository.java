package com.iorga.debattons.apiserver.reaction;

import org.springframework.data.gremlin.repository.GremlinRepository;
import org.springframework.data.gremlin.annotation.*;

public interface ReactionRepository extends GremlinRepository<Reaction>{

    @Query(value = "graph.V().has('firstName', ?)")
    Reaction findRootReaction();

}