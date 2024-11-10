package com.foolish.moviereservation.service;

import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.repository.MovieRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
  private final MovieRepo movieRepo;

  public Movie save(Movie movie) {
    return movieRepo.save(movie);
  }

  public Movie findMovieByIdOrElseThrow(Integer id) {
    Optional<Movie> result = movieRepo.findMovieById(id);
    return result.orElseThrow(() -> new ResourceNotFoundException("Movie not found",null));
  }
}
