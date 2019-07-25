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
        assertThat(createdDisagreeChild.getTotalChildrenCount()).isEqualTo(0);
        assertThat(createdRoot.getSupportScore()).isEqualTo(-ReactionService.DEFAULT_TYPE_LEVEL);
        assertThat(createdRoot.getTotalChildrenCount()).isEqualTo(1);

        int agreeLevel = 100;
        Reaction agreeChild = new Reaction()
            .type(ReactionType.AGREE)
            .typeLevel(agreeLevel)
            .title("Agree 100")
            .parentReaction(createdRoot);

        Reaction createdAgreeChild = reactionService.saveByUser(agreeChild, user);
        assertThat(createdAgreeChild.getSupportScore()).isNull();
        assertThat(createdRoot.getSupportScore()).isEqualTo(-ReactionService.DEFAULT_TYPE_LEVEL + agreeLevel);
        assertThat(createdRoot.getTotalChildrenCount()).isEqualTo(2);

        int agreeToAgreeLevel = 50;
        Reaction agreeToAgreeChild = new Reaction()
            .type(ReactionType.AGREE)
            .typeLevel(agreeToAgreeLevel)
            .title("Agree 50 to agree 100")
            .parentReaction(createdAgreeChild);

        Reaction createdAgreeToAgreeChild = reactionService.saveByUser(agreeToAgreeChild, user);
        assertThat(createdAgreeToAgreeChild.getSupportScore()).isNull();
        assertThat(createdRoot.getSupportScore()).isEqualTo(-ReactionService.DEFAULT_TYPE_LEVEL + agreeLevel + agreeToAgreeLevel);
        assertThat(createdRoot.getTotalChildrenCount()).isEqualTo(3);
        assertThat(createdAgreeChild.getSupportScore()).isEqualTo(agreeToAgreeLevel);
        assertThat(createdAgreeChild.getTotalChildrenCount()).isEqualTo(1);

        int agreeToDisagreeLevel = 80;
        Reaction agreeToDisagreeChild = new Reaction()
            .type(ReactionType.AGREE)
            .typeLevel(agreeToDisagreeLevel)
            .title("Agree 80 to default disagree")
            .parentReaction(disagreeChild);

        Reaction createdAgreeToDisagree = reactionService.saveByUser(agreeToDisagreeChild, user);
        assertThat(createdAgreeToDisagree.getSupportScore()).isNull();
        assertThat(createdDisagreeChild.getSupportScore()).isEqualTo(agreeToDisagreeLevel);
        assertThat(createdRoot.getSupportScore()).isEqualTo(-ReactionService.DEFAULT_TYPE_LEVEL + agreeLevel + agreeToAgreeLevel - agreeToDisagreeLevel);
        assertThat(createdRoot.getTotalChildrenCount()).isEqualTo(4);
    }
}
