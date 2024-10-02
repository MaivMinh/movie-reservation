package com.foolish.moviereservation.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile("!prod")
public class NonProdUsernamePwdAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    try {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new AuthenticationException("Authentication failed!") {
      };
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
