package com.iorga.debattons.apiserver.reaction;

import com.iorga.debattons.apiserver.test.AbstractControllerTest;
import com.iorga.debattons.apiserver.user.User;
import com.iorga.debattons.apiserver.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
public class ReactionControllerTest extends AbstractControllerTest {

  @Autowired
  private ReactionService reactionService;

  @Autowired
  private UserService userService;

  @Test
  public void createTest() throws Exception {
    User testUser = createTestUserUsingService();
    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction, testUser);
    assertThat(reactionOut.getTitle()).isEqualTo(reaction.getTitle());
    assertThat(reactionOut.getContent()).isEqualTo(reaction.getContent());
    assertThat(reactionOut.getId()).isNotEmpty();
  }

  @Test
  public void createTestWithUnknownUser() throws Exception {
    createTestUserUsingService();
    Reaction reaction = newTestReaction();
    User unknownUser = new User();
    unknownUser.setLogin("unknownUser");
    unknownUser.setPassword("unknownUserPass");
    try {
      createReaction(reaction, unknownUser);
      fail("Should have thrown 401 Unauthorized error");
    } catch (ResourceAccessException e) {
      Throwable cause = e.getCause();
      assertThat(cause).isInstanceOf(HttpRetryException.class);
      assertThat(((HttpRetryException) cause).responseCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
  }

  private User createTestUserUsingService() throws Exception {
    User testUser = new User();
    testUser.setLogin("test");
    testUser.setPassword("testpass");
    testUser.setEmail("test@example.tld");
    return userService.create(testUser);
  }

  private Reaction createReaction(Reaction reaction, User createdByUser) {
    return restTemplate.withBasicAuth(createdByUser.getLogin(), createdByUser.getPassword()).postForObject("/reactions", reaction, Reaction.class);
  }

  private Reaction createReaction(Reaction reaction, String reactingToReactionId, User testUser) throws UnsupportedEncodingException {
    return restTemplate.withBasicAuth(testUser.getLogin(), testUser.getPassword()).postForObject("/reactions?reactToReactionId=" + URLEncoder.encode(reactingToReactionId, "UTF-8"), reaction, Reaction.class);
  }

  private Reaction createReactionUsingService(Reaction reaction, User createdByUser) throws Exception {
    return reactionService.create(reaction, createdByUser.getLogin());
  }

  private Reaction createReactionUsingService(Reaction reaction, String reactingToReactionId, User createdByUser) throws Exception {
    return reactionService.createByReactionReactingToReactionId(reaction, reactingToReactionId, null, createdByUser.getLogin());
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
  public void createReactingToTest() throws Exception {
    User testUser = createTestUserUsingService();

    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReaction(reaction, testUser);

    Reaction reaction2 = newTestReaction(" 2");
    Reaction reactionOut2 = createReaction(reaction2, reactionOut.getId(), testUser);

    assertThat(reactionOut2.getTitle()).isEqualTo(reaction2.getTitle());
    assertThat(reactionOut2.getContent()).isEqualTo(reaction2.getContent());
    assertThat(reactionOut2.getId()).isNotEmpty();
  }

  @Test
  public void findRootsTest() throws Exception {
    User testUser = createTestUserUsingService();

    Reaction reaction = newTestReaction();
    createReactionUsingService(reaction, testUser);
    reaction.setTitle("Test title 2");
    createReactionUsingService(reaction, testUser);
    List<Reaction> reactions = restTemplate.exchange("/reactions/roots", HttpMethod.GET, null, new ParameterizedTypeReference<List<Reaction>>() {
    }).getBody();
    assertThat(reactions).hasSize(2);
    assertThat(reactions.get(0).getTitle()).isEqualTo(reaction.getTitle()); // Latest reaction must be the first one to be listed
  }

  @Test
  public void findByIdTest() throws Exception {
    User testUser = createTestUserUsingService();

    Reaction reaction = newTestReaction();
    Reaction createdReaction = createReactionUsingService(reaction, testUser);
    Reaction foundReaction = restTemplate.getForObject("/reactions/" + createdReaction.getId(), Reaction.class);
    assertThat(foundReaction.getTitle()).isEqualTo(reaction.getTitle());
  }

  @Test
  public void findByIdLoadingReactedToDepthTest() throws Exception {
    User testUser = createTestUserUsingService();

    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReactionUsingService(reaction, testUser);
    createReactionUsingService(newTestReaction(" 2"), reactionOut.getId(), testUser);
    createReactionUsingService(newTestReaction(" 3"), reactionOut.getId(), testUser);

    Reaction foundReaction = restTemplate.getForObject("/reactions/" + reactionOut.getId() + "?reactedToDepth=1", Reaction.class);
    assertThat(foundReaction.getTitle()).isEqualTo(reaction.getTitle());
    assertThat(foundReaction.getReactedTo()).hasSize(2);
  }

  @Test
  public void agreeWithByIdTest() throws Exception {
    // First we must create a user FIXME this user must be used from the "current session"
    User user = new User();
    user.setLogin("agreeWithByIdTest");
    user.setEmail("agreeWithByIdTest@example.tld");
    user.setPassword("agreeWithByIdTest");
    restTemplate.postForObject("/users", user, User.class);

    Reaction reaction = newTestReaction();
    Reaction reactionOut = createReactionUsingService(reaction, user);

    ResponseEntity<Void> voidResponseEntity = restTemplate.withBasicAuth(user.getLogin(), user.getPassword()).postForEntity("/reactions/" + reactionOut.getId() + "?reactionType=agree", null, Void.class);
    assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
