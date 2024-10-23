package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.mapper.UserMapper;
import com.foolish.moviereservation.model.User;
import com.foolish.moviereservation.records.ApiResponse;
import com.foolish.moviereservation.service.RoleService;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  // Hàm lấy thông tin của User sau khi login thành công.
  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUserDetails(@PathVariable Integer userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Trong ContextHolder sẽ chứa đối tượng xác thực thành công.
    UserDTO dto = userService.findByUserId(userId);
    if (dto == null || dto.getUserId() <= 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, HttpStatus.NOT_FOUND.value(), "User not found", null));
    }
    if (!dto.getUsername().equals(authentication.getName())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, HttpStatus.FORBIDDEN.value(), "Forbidden", null));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, HttpStatus.OK.value(), "Find one success", dto));
  }
}
