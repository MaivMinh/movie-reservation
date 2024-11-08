package com.foolish.moviereservation.security;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.service.UserRoleService;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OwnUserDetailsService implements UserDetailsService {
  // Class chịu trách nhiệm load thông tin của User lên hệ thống. Được sử dụng trong AuthenticationProvider.
  // -> Việc tạo thêm class này cũng chính đang triển khai nguyên lý Single-Responsibility và Open/Closed.
  private final UserService userService;
  private final UserRoleService userRoleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.foolish.moviereservation.model.User user = userService.findUserByUsername(username);
    if (user != null && user.getId() > 0) {
      List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + ((com.foolish.moviereservation.model.User) user).getRole().getName()));
      return new User(username, user.getPassword(), roles );
    }
    throw new UsernameNotFoundException("Failed to load user by username");
  }
}
