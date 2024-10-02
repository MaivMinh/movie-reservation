package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
  User findByUsername(String username);
  User findByEmail(String email);
  User findByPhoneNumber(String email);
}
