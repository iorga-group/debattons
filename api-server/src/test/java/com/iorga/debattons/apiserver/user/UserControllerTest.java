package com.iorga.debattons.apiserver.user;

import com.iorga.debattons.apiserver.test.AbstractControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserControllerTest extends AbstractControllerTest {

  @Test
  public void postTest() {
    User user = new User();
    user.setLogin("test");
    user.setEmail("test@test.tld");
    user.setPassword("test");

    User createdUser = restTemplate.postForObject("/users", user, User.class);

    assertThat(createdUser.getId()).isNotEmpty();
    assertThat(createdUser.getLogin()).isEqualTo(user.getLogin());
    assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
    assertThat(createdUser.getPassword()).isNullOrEmpty();
  }
}
