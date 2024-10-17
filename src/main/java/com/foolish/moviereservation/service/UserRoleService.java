package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.repository.UserRoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {
  private final UserRoleRepo repo;

  public UserRole findByUserAndRole(User user, Role role) {
    return repo.findByUserAndRole(user, role);
  }

  public UserRole save(UserRole userRole) {
    if (userRole.getUser() == null || userRole.getRole() == null) throw new IllegalArgumentException("User and Role are required");
    return repo.save(userRole);
  }

  public List<UserRole> findAllByUser(User user) {
    return repo.findAllByUser(user);
  }
}
