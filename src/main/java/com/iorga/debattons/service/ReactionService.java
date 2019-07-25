package com.iorga.debattons.service;

import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.User;
import com.iorga.debattons.domain.enumeration.ReactionType;
import com.iorga.debattons.repository.ReactionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

@Service
@Transactional
public class ReactionService {

    public static final int MAX_TYPE_LEVEL = 120;
    public static final int DEFAULT_TYPE_LEVEL = MAX_TYPE_LEVEL / 2;

    private final ReactionRepository reactionRepository;


    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }


    public Reaction saveByUser(@Valid Reaction reaction, User user) {
        Long reactionId = reaction.getId();
        if (reactionId == null) {
            // creation
            reaction.setCreator(user);
            reaction.setTotalChildrenCount(0);
            Reaction parentReaction = reaction.getParentReaction();
            if (parentReaction != null) {
                Reaction parentReactionInDb = reactionRepository.getOne(parentReaction.getId());
                parentReactionInDb.addChildrenReactions(reaction);  // In order for the parent reaction not to be modified
            }
        } else {
            // modification
            Reaction reactionInDb = findOneByIdForModificationByUser(reactionId, user).get();
            // Only allow modifying the following fields
            reactionInDb
                .title(reaction.getTitle())
                .content(reaction.getContent())
                .type(reaction.getType())
                .typeLevel(reaction.getTypeLevel())
                ;
            reaction = reactionInDb;
        }
        Reaction savedReaction = reactionRepository.save(reaction);
        // Now (re-)compute support score
        computeSupportScoreUsingChildrenAndRecountChildren(savedReaction);
        // And all its parents one
        for(Reaction parentReaction = savedReaction.getParentReaction() ; parentReaction != null ; parentReaction = parentReaction.getParentReaction()) {
            computeSupportScoreUsingChildrenAndRecountChildren(parentReaction);
        }

        return savedReaction;
    }

    private static final EnumSet<ReactionType> AGREE_DISAGREE = EnumSet.of(ReactionType.AGREE, ReactionType.DISAGREE);

    private void computeSupportScoreUsingChildrenAndRecountChildren(Reaction reaction) {
        Set<Reaction> childrenReactions = reaction.getChildrenReactions();
        if (childrenReactions != null && childrenReactions.size() > 0) {
            double supportScore = 0;
            int totalSupportChildrenCount = 0;
            for (Reaction childReaction : childrenReactions) {
                ReactionType childReactionType = childReaction.getType();
                if (AGREE_DISAGREE.contains(childReactionType)) {
                    totalSupportChildrenCount += 1 + childReaction.getTotalChildrenCount();

                    boolean isDisagree = ReactionType.DISAGREE.equals(childReactionType);
                    Double childSupportScore = childReaction.getSupportScore();
                    if (childSupportScore != null) {
                        supportScore += childSupportScore * (isDisagree ? -1D : 1D);
                    }
                    Integer childTypeLevel = childReaction.getTypeLevel();
                    if (childTypeLevel == null) {
                        childTypeLevel = DEFAULT_TYPE_LEVEL;
                    }
                    supportScore += childTypeLevel * (isDisagree ? -1 : 1);
                }
            }
            reaction.setSupportScore(supportScore);
            reaction.setTotalChildrenCount(totalSupportChildrenCount);
        } else {
            reaction.setSupportScore(null);
            reaction.setTotalChildrenCount(0);
        }
    }

    private Optional<Reaction> findOneByIdForModificationByUser(Long reactionId, User user) {
        return reactionRepository.findById(reactionId).map(reaction -> {
            if (!reaction.getCreator().equals(user)) {
                throw new AccessDeniedException("User can't edit the reaction of another user");
            } else {
                return reaction;
            }
        });
    }

    public void deleteByIdAndUser(Long id, User user) {
        findOneByIdForModificationByUser(id, user).ifPresent(reaction -> reactionRepository.delete(reaction));
    }
}
