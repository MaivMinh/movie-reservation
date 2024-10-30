package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.model.CreatedMovie;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/admin/movies")
@AllArgsConstructor
public class AdminMovieController {
  private S3Service s3Service;

  @PostMapping(value = "")
  public ResponseEntity<ResponseData> createMovie(@RequestBody CreatedMovie movie) throws IOException {

    return null;
  }
}
