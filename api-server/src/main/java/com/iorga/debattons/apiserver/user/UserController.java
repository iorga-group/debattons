package com.iorga.debattons.apiserver.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {
  private UserService userService;


  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public User post(@RequestBody User user) throws Exception {
    return userService.create(user);
  }
}
