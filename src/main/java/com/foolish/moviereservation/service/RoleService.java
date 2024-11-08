package com.foolish.moviereservation.service;

import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.repository.RoleRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {
  private final RoleRepo repo;

  public Role findByRoleId(Integer id) {
    Optional<Role> result = repo.findRoleById(id);
    return result.orElseThrow(() -> new ResourceNotFoundException("Role not found", Map.of("id", String.valueOf(id))));
  }
}
