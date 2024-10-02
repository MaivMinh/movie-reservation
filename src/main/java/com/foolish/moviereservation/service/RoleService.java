package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.repository.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
  private final RoleRepo repo;

  public Role findByRoleId(Integer id) {
    return repo.findByRoleId(id);
  }
}
