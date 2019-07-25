package com.iorga.debattons.service;

import com.iorga.debattons.DebattonsApp;
import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.User;
import com.iorga.debattons.domain.enumeration.ReactionType;
import com.iorga.debattons.repository.ReactionRepository;
import com.iorga.debattons.repository.UserRepository;
import com.iorga.debattons.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DebattonsApp.class)
public class ReactionServiceIT {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    public void testSupportScoring() {
        Reaction root = new Reaction()
            .type(ReactionType.ROOT)
            .title("Root");

        User user = TestUtils.getUser(userRepository);

        Reaction createdRoot = reactionService.saveByUser(root, user);
        assertThat(createdRoot.getSupportScore()).isNull();

        Reaction disagreeChild = new Reaction()
            .type(ReactionType.DISAGREE)
            .title("Default disagree")
            .parentReaction(createdRoot);

        Reaction createdDisagreeChild = reactionService.saveByUser(disagreeChild, user);
        assertThat(createdDisagreeChild.getSupportScore()).isNull();
        assertThat(reactionRepository.getOne(createdRoot.getId()).getSupportScore()).isEqualTo(-ReactionService.DEFAULT_TYPE_LEVEL);

        Reaction agreeChild = new Reaction()
            .type(ReactionType.AGREE)
            .typeLevel(100)
            .title("Agree 100")
            .parentReaction(createdRoot);

        Reaction createdAgreeChild = reactionService.saveByUser(agreeChild, user);
        assertThat(createdAgreeChild.getSupportScore()).isNull();
        assertThat(reactionRepository.getOne(createdRoot.getId()).getSupportScore()).isEqualTo((-(float)ReactionService.DEFAULT_TYPE_LEVEL + 100F) / 2F);

        Reaction agreeToAgreeChild = new Reaction()
            .type(ReactionType.AGREE)
            .typeLevel(50)
            .title("Agree 50")
            .parentReaction(createdAgreeChild);

        Reaction createdAgreeToAgreeChild = reactionService.saveByUser(agreeToAgreeChild, user);
        assertThat(createdAgreeToAgreeChild.getSupportScore()).isNull();
        assertThat(reactionRepository.getOne(createdRoot.getId()).getSupportScore()).isEqualTo((float)agreeChild.getTypeLevel());
        assertThat(reactionRepository.getOne(createdAgreeChild.getId()).getSupportScore()).isEqualTo((float)agreeToAgreeChild.getTypeLevel());
    }
}
