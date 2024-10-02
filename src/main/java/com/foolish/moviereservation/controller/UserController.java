package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.Role;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.model.UserRole;
import com.foolish.moviereservation.service.RoleService;
import com.foolish.moviereservation.service.UserRoleService;
import com.foolish.moviereservation.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final RoleService roleService;

  public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) {
    // Hàm tạo một User mới bên trong hệ thống.
    List<UserDTO> result = null;
    result = userService.findUserByUserName(user.getUsername());
    if (result.isEmpty()) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    result = userService.findAllByEmail(user.getEmail());
    if (result.isEmpty()) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    result = userService.findAllByPhoneNumber(user.getPhoneNumber());
    if (result.isEmpty()) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    // Set Role cho User. [{1: ADMIN}, {2, USER}]
    Role role = roleService.findByRoleId(Role.USER);
    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(role);
    user.setUserRoles(Collections.singletonList(userRole));

    // Hashing user's password.
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    try {
      user = userService.save(user);
    } catch (RuntimeException e) {
      log.error("Failed to create user: {}", user.getUsername(), e);
      return ResponseEntity.internalServerError().body(null);
    }

    if (user.getUserId() > 0) {
      UserDTO userDTO = userMapper.toDTO(user);
      return ResponseEntity.ok(userDTO);
    }
    return ResponseEntity.internalServerError().body(null);
  }
}
