package com.foolish.moviereservation.service;

import com.foolish.moviereservation.model.Showtime;
import com.foolish.moviereservation.repository.ShowtimeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShowtimeService {
  private final ShowtimeRepo showtimeRepo;


  public Showtime save(Showtime showtime) {
    return showtimeRepo.save(showtime);
  }
}
