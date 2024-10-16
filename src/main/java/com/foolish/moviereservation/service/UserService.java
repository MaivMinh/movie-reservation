package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepo repo;
  private final UserMapper mapper;

  public UserDTO findUserByUserName(String username) {
    return mapper.toDTO(repo.findByUsername(username));
  }

  public UserDTO findUserByEmail(String email) {
    return mapper.toDTO(repo.findByEmail(email));
  }

  public UserDTO findUserByPhoneNumber(String phoneNumber) {
    return mapper.toDTO(repo.findByPhoneNumber(phoneNumber));
  }

  // Không được xoá vì sẽ ảnh hưởng tới OwnUserDetailsService class.
  public User findByUsername(String username) {
    return repo.findByUsername(username);
  }

  public User save(User user) {
    return repo.save(user);
  }

  public UserDTO findByUserId(Integer userId) {
    return mapper.toDTO(repo.findByUserId(userId));
  }
}
