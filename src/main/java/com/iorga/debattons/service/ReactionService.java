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
    private static final BigDecimal MAX_TYPE_LEVEL_BD = new BigDecimal(MAX_TYPE_LEVEL);
    public static final int DEFAULT_TYPE_LEVEL = MAX_TYPE_LEVEL / 2;
    private static final BigDecimal BIG_DECIMAL_TWO = new BigDecimal(2);

    private final ReactionRepository reactionRepository;


    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }


    public Reaction saveByUser(@Valid Reaction reaction, User user) {
        Long reactionId = reaction.getId();
        if (reactionId == null) {
            // creation
            reaction.setCreator(user);
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
        computeSupportScoreUsingChildren(savedReaction);
        // And all its parents one
        for(Reaction parentReaction = savedReaction.getParentReaction() ; parentReaction != null ; parentReaction = parentReaction.getParentReaction()) {
            computeSupportScoreUsingChildren(parentReaction);
        }

        return savedReaction;
    }

    private static final EnumSet<ReactionType> AGREE_DISAGREE = EnumSet.of(ReactionType.AGREE, ReactionType.DISAGREE);

    private static class PonderedSupportLevel {
        /**
         * A decimal between 0 and 2 that will ponder the attached support level, meaning it will represent between 0 and 2 times its support level
         */
        private BigDecimal ponderation;
        /**
         * Support level between -MAX_TYPE_LEVEL and +MAX_TYPE_LEVEL
         */
        private int supportLevel;

        private static PonderedSupportLevel forReaction(Reaction reaction) {
            ReactionType reactionType = reaction.getType();
            if (AGREE_DISAGREE.contains(reactionType)) {
                PonderedSupportLevel ponderedSupportLevel = new PonderedSupportLevel();
                ponderedSupportLevel.supportLevel = DEFAULT_TYPE_LEVEL;
                Integer typeLevel = reaction.getTypeLevel();
                if (typeLevel != null) {
                    ponderedSupportLevel.supportLevel = typeLevel;
                }
                if (ReactionType.DISAGREE.equals(reactionType)) {
                    ponderedSupportLevel.supportLevel *= -1;
                }
                Double supportScore = reaction.getSupportScore();
                if (supportScore != null) {
                    ponderedSupportLevel.ponderation = new BigDecimal(supportScore).add(MAX_TYPE_LEVEL_BD).divide(MAX_TYPE_LEVEL_BD, MathContext.DECIMAL128); // According that the supportScore is between -MAX_TYPE_LEVEL and +MAX_TYPE_LEVEL
                } else {
                    ponderedSupportLevel.ponderation = BigDecimal.ONE; // by default, a support is pondered as 1
                }
                return ponderedSupportLevel;
            } else {
                return null;
            }
        }
    }

    private void computeSupportScoreUsingChildren(Reaction reaction) {
        List<PonderedSupportLevel> childrenPonderedSupportLevels = new ArrayList<>(reaction.getChildrenReactions().size());
        BigDecimal ponderationSum = BigDecimal.ZERO;

        for (Reaction childReaction : reaction.getChildrenReactions()) {
            PonderedSupportLevel ponderedSupportLevel = PonderedSupportLevel.forReaction(childReaction);
            if (ponderedSupportLevel != null) {
                childrenPonderedSupportLevels.add(ponderedSupportLevel);
                ponderationSum = ponderationSum.add(ponderedSupportLevel.ponderation);
            }
        }

        if (!childrenPonderedSupportLevels.isEmpty()) {
            // First sort the list of scores by there support level
            childrenPonderedSupportLevels.sort(Comparator.comparingInt(a -> a.supportLevel));
            // Then try to obtain the median value of those supports, weighting each support with its ponderation
            BigDecimal medianPonderation = ponderationSum.divide(BIG_DECIMAL_TWO);
            int selectedIndex = 0;
            BigDecimal ponderationIterationSum = BigDecimal.ZERO;
            PonderedSupportLevel selectedPonderedSupportLevel = null;
            for (; selectedIndex < childrenPonderedSupportLevels.size() && ponderationIterationSum.compareTo(medianPonderation) < 0; selectedIndex++) {
                selectedPonderedSupportLevel = childrenPonderedSupportLevels.get(selectedIndex);
                ponderationIterationSum = ponderationIterationSum.add(selectedPonderedSupportLevel.ponderation);
            }
            double supportScore;
            if (selectedPonderedSupportLevel == null) {
                // Every children had a ponderation of 0, so we cannot estimate the current score
                reaction.setSupportScore(null);
            } else {
                if (ponderationIterationSum.compareTo(medianPonderation) == 0) {
                    // the cursor is between two values, get the value between them
                    supportScore = new BigDecimal(selectedPonderedSupportLevel.supportLevel + childrenPonderedSupportLevels.get(selectedIndex).supportLevel).divide(BIG_DECIMAL_TWO).doubleValue();
                } else {
                    supportScore = selectedPonderedSupportLevel.supportLevel;
                }
                reaction.setSupportScore(supportScore);
            }
        } else {
            reaction.setSupportScore(null);
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
