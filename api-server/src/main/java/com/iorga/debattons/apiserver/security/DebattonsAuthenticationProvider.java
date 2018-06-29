package com.iorga.debattons.apiserver.security;

import com.google.common.collect.Lists;
import com.iorga.debattons.apiserver.user.User;
import com.iorga.debattons.apiserver.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

// Inspired by http://www.baeldung.com/spring-security-authentication-provider
@Component
public class DebattonsAuthenticationProvider implements AuthenticationProvider {
  @Autowired
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();
    try {
      Optional<User> optionalUser = userService.findUserByLogin(name);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        if (Arrays.equals(UserService.computePasswordHash(password, user.getSalt()), user.getPasswordHash())) {
          Collection<? extends GrantedAuthority> authorities = Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER"));
          return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
          throw new BadCredentialsException("Password does not match for user with login "+name+".");
        }
      } else {
        throw new UsernameNotFoundException("User with login "+name+" not found.");
      }
    } catch (Exception e) {
      throw new InternalAuthenticationServiceException("Problem while finding the user", e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
