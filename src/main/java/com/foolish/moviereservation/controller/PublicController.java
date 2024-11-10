package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/", "/home"})
public class PublicController {

  @RequestMapping(value = "/")
  public ResponseEntity<ResponseData> getHomePage() {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), "Success", null));
  }
}
