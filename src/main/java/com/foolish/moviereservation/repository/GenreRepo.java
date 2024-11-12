package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepo extends JpaRepository<Genre, Integer> {
  Optional<Genre> findById(Integer id);
}
