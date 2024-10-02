package com.foolish.moviereservation.security;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.service.UserRoleService;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnUserDetailsService implements UserDetailsService {
  // Class chịu trách nhiệm load thông tin của User lên hệ thống. Được sử dụng trong AuthenticationProvider.
  // -> Việc tạo thêm class này cũng chính đang triển khai nguyên lý Single-Responsibility và Open/Closed.
  private final UserService userService;
  private final UserRoleService userRoleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username);
    if (user != null && user.getUserId() > 0) {
      List<UserRole> userRoles = userRoleService.findAllByUser(user);
      List<Role> roles = userRoles.stream().map(UserRole::getRole).toList();
      List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
      return new org.springframework.security.core.userdetails.User(username,user.getPassword(), authorities);
    }
    throw new UsernameNotFoundException("Failed to load user by username");
  }
}
