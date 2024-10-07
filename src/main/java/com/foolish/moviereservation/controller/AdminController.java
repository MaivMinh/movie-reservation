package com.foolish.moviereservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

  @GetMapping(value = "/greeting")
  public ResponseEntity<String> greeting() {
    return ResponseEntity
            .ok()
            .body("Hello admin");
  }
}
