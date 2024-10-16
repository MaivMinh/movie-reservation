package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.mapper.UserMapperImpl;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
  private final UserService userService;
  private final UserMapper userMapper;


  @GetMapping("")
  public ResponseEntity<UserDTO> getAdminDetails(Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username);
    UserDTO userDTO = userMapper.toDTO(user);
    return ResponseEntity.ok(userDTO);
  }

}
