package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.mapper.UserMapperImpl;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepo repo;
  private final UserMapper mapper;

  public UserDTO findUserByUserNameOrElseThrow(String username) {
    Optional<User> result = repo.findByUsername(username);
    User user = result.orElseThrow(() -> new ResourceNotFoundException("Username not found", Map.of("username", username)));
    return mapper.toDTO(user);
  }

  public UserDTO findUserDTOByUsername(String username) {
    Optional<User> result = repo.findByUsername(username);
    return result.map(mapper::toDTO).orElse(null);
  }

  public UserDTO findUserByEmailOrElseThrow(String email) {
    Optional<User> result = repo.findByEmail(email);
    User user = result.orElseThrow(() -> new ResourceNotFoundException("Email not found", Map.of("email", email)));
    return mapper.toDTO(user);
  }

  public UserDTO findUserByEmail(String email) {
    Optional<User> result = repo.findByEmail(email);
    return result.map(mapper::toDTO).orElse(null);
  }

  // Không được xoá vì sẽ ảnh hưởng tới OwnUserDetailsService class.
  public User findUserByUsername(String username) {
    Optional<User> result = repo.findByUsername(username);
    return result.orElseThrow(() -> new ResourceNotFoundException("Username not found", Map.of("username", username)));
  }

  public User save(User user) {
    return repo.save(user);
  }
}
