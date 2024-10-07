package com.foolish.moviereservation.security;

import com.foolish.moviereservation.service.UserRoleService;
import com.foolish.moviereservation.service.UserService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile("prod")
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService detailsService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // Hàm thực hiện xác thực {username, password} của User khi thực hiện Sign-In.
    String username = authentication.getName();
    String rawPassword = authentication.getPrincipal().toString();

    try {
      UserDetails userDetails = detailsService.loadUserByUsername(username);
      boolean isMatched = passwordEncoder.matches(rawPassword, userDetails.getPassword());
      if (isMatched) {
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
      }
      return new UsernamePasswordAuthenticationToken(username, rawPassword);
    } catch (UsernameNotFoundException e1) {
      log.error(e1.getMessage());
      throw e1;
    } catch (Exception e) {
      log.error("Failed to authenticate user with email: {}", username, e);
      throw new AuthenticationException("Authentication failed!") {
      };
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
