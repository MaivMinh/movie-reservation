package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepo repo;
  private final UserMapper mapper;

  public List<UserDTO> findUserByUserName(String username) {
    return repo.findAllByUsername(username).stream().map(mapper::toDTO).toList();
  }

  public List<UserDTO> findAllByEmail(String email) {
    return repo.findAllByEmail(email).stream().map(mapper::toDTO).toList();
  }

  public List<UserDTO> findAllByPhoneNumber(String phoneNumber) {
    return repo.findAllByPhoneNumber(phoneNumber).stream().map(mapper::toDTO).toList();
  }

  public User findByUsername(String username) {
    return repo.findByUsername(username);
  }
  
  public User save(User user) {
    return repo.save(user);
  }
}
