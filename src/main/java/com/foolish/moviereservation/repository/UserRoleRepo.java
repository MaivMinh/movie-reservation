package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
  UserRole findByUserAndRole(User user, Role role);
  List<UserRole> findAllByUser(User user);
}
