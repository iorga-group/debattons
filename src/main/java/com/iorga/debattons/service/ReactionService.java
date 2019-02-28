package com.iorga.debattons.service;

import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.User;
import com.iorga.debattons.repository.ReactionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
public class ReactionService {


    private final ReactionRepository reactionRepository;


    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }


    public Reaction saveByUser(@Valid Reaction reaction, User user) {
        if (reaction.getId() == null) {
            // creation
            reaction.setCreator(user);
        } else {
            // modification
            Reaction reactionInDb = reactionRepository.findById(reaction.getId()).get();
            if (!reactionInDb.getCreator().equals(user)) {
                throw new AccessDeniedException("User can't edit the reaction of another user");
            }
            reaction
                .title(reactionInDb.getTitle())
                .content(reactionInDb.getContent())
                ;
        }
        return reactionRepository.save(reaction);
    }

}
