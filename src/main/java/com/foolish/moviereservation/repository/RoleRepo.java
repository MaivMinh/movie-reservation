package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
  Optional<Role> findRoleById(Integer id);
}
