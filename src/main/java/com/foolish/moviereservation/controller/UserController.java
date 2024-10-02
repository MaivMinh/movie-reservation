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


}
