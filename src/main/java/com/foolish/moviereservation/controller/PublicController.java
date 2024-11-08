package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class PublicController {

  @RequestMapping("/app-info")
  public ResponseEntity<ResponseData> getApplicationInfo() {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), "Success", null));
  }

  @RequestMapping(value = {"/", "/home"})
  public ResponseEntity<ResponseData> getHomePage() {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), "Success", null));
  }
}
