package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
  List<User> findAllByUsername(String username);
  List<User> findAllByEmail(String email);
  List<User> findAllByPhoneNumber(String email);
  User findByUsername(String username);
}
