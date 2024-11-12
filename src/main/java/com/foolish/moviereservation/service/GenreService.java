package com.foolish.moviereservation.service;

import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.model.Genre;
import com.foolish.moviereservation.repository.GenreRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService {
  private final GenreRepo genreRepo;


  public Genre findGenreByIdOrElseThrow(Integer id) {
    Optional<Genre> result = genreRepo.findById(id);
    return result.orElseThrow(() -> new ResourceNotFoundException("Genre not found", Map.of("id", String.valueOf(id))));
  }
}
