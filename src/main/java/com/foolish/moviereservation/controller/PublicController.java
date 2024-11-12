package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/", "/home"})
@AllArgsConstructor
public class PublicController {
  private final MovieService movieService;

  @RequestMapping(value = "")
  public ResponseEntity<ResponseData> getHomePage() {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), "Success", Map.of("movies", "")));
  }
}
