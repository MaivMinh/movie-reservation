package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.service.RoleService;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final RoleService roleService;

  // Hàm lấy thông tin của User sau khi login thành công.
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserDetails(@PathVariable Integer userId, Authentication authentication) {
    UserDTO dto = userService.findByUserId(userId);
    if (dto == null || dto.getUserId() <= 0)  {
      return ResponseEntity.notFound().build();
    }
    if (!dto.getUsername().equals(authentication.getName())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return ResponseEntity.ok(dto);
  }
}
