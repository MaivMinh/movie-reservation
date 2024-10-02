package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
  Role findByRoleId(Integer id);
}
