package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Cinema;
import com.foolish.moviereservation.repository.CinemaRepo;
import com.foolish.moviereservation.response.ResponseData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class CinemaService {

  private final CinemaRepo cinemaRepo;

  public Cinema save(@Valid Cinema cinema) {
    return cinemaRepo.save(cinema);
  }
}
