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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  private final UserService userService;

  // Hàm lấy thông tin của User sau khi login thành công.
  @GetMapping("/profile")
  public ResponseEntity<ResponseData> getUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDTO dto = userService.findUserByUserNameOrElseThrow(authentication.getName());
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", dto));
  }
}
