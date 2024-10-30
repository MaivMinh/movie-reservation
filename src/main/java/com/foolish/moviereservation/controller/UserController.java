package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.response.ResponseError;
import com.foolish.moviereservation.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public ResponseEntity<ResponseData> getUserDetails(@PathVariable Integer userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Trong ContextHolder sẽ chứa đối tượng xác thực thành công.
    UserDTO dto = userService.findByUserId(userId);
    if (dto == null || dto.getUserId() <= 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(HttpStatus.NOT_FOUND.value(), "Resource not found"));
    }
    if (!dto.getUsername().equals(authentication.getName())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseError(HttpStatus.FORBIDDEN.value(), "Forbidden"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), "Find one success", dto));
  }
}
