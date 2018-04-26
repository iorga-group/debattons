package com.iorga.debattons.apiserver.reaction;

import com.iorga.debattons.apiserver.test.AbstractControllerTest;
import com.iorga.debattons.apiserver.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ReactionControllerTest extends AbstractControllerTest {

  @Test
  public void createTest() throws IOException {
    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction);
    assertThat(reactionOut.getTitle()).isEqualTo(reaction.getTitle());
    assertThat(reactionOut.getContent()).isEqualTo(reaction.getContent());
    assertThat(reactionOut.getId()).isNotEmpty();
  }

  private Reaction createReaction(Reaction reaction) {
    return restTemplate.postForObject("/reactions", reaction, Reaction.class);
  }

  private Reaction createReaction(Reaction reaction, String reactingToReactionId) throws UnsupportedEncodingException {
    return restTemplate.postForObject("/reactions?reactToReactionId=" + URLEncoder.encode(reactingToReactionId, "UTF-8"), reaction, Reaction.class);
  }

  private Reaction newTestReaction() {
    return newTestReaction("");
  }

  private Reaction newTestReaction(String label) {
    Reaction reaction = new Reaction();
    reaction.setTitle("Test Title" + label);
    reaction.setContent("Test content lorem ipsum" + label);
    return reaction;
  }

  @Test
  public void createReactingToTest() throws IOException {
    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction);

    Reaction reaction2 = newTestReaction(" 2");
    Reaction reactionOut2 = createReaction(reaction2, reactionOut.getId());

    assertThat(reactionOut2.getTitle()).isEqualTo(reaction2.getTitle());
    assertThat(reactionOut2.getContent()).isEqualTo(reaction2.getContent());
    assertThat(reactionOut2.getId()).isNotEmpty();
  }

  @Test
  public void findRootsTest() {
    Reaction reaction = newTestReaction();
    createReaction(reaction);
    reaction.setTitle("Test title 2");
    createReaction(reaction);
    List<Reaction> reactions = restTemplate.exchange("/reactions/roots", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reaction>>() {
    }).getBody();
    assertThat(reactions).hasSize(2);
    assertThat(reactions.get(0).getTitle()).isEqualTo(reaction.getTitle()); // Latest reaction must be the first one to be listed
  }

  @Test
  public void findByIdTest() {
    Reaction reaction = newTestReaction();
    Reaction createdReaction = createReaction(reaction);
    Reaction foundReaction = restTemplate.getForObject("/reactions/" + createdReaction.getId(), Reaction.class);
    assertThat(foundReaction.getTitle()).isEqualTo(reaction.getTitle());
  }

  @Test
  public void findByIdLoadingReactedToDepthTest() throws UnsupportedEncodingException {
    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction);
    Reaction reaction2 = newTestReaction(" 2");
    Reaction reactionOut2 = createReaction(reaction, reactionOut.getId());
    Reaction reaction3 = newTestReaction(" 3");
    Reaction reactionOut3 = createReaction(reaction, reactionOut.getId());

    Reaction foundReaction = restTemplate.getForObject("/reactions/" + reactionOut.getId() + "?reactedToDepth=1", Reaction.class);
    assertThat(foundReaction.getTitle()).isEqualTo(reaction.getTitle());
    assertThat(foundReaction.getReactedTo()).hasSize(2);
  }

  @Test
  public void agreeWithByIdTest() {
    // First we must create a user FIXME this user must be used from the "current session"
    User user = new User();
    user.setLogin("test");
    user.setEmail("test@test.tld");
    user.setPassword("test");
    restTemplate.postForObject("/users", user, User.class);

    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction);

    ResponseEntity<Void> voidResponseEntity = restTemplate.postForEntity("/reactions/" + reactionOut.getId() + "?reactionType=agree", null, Void.class);
    assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
