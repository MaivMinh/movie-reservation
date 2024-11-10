package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer> {
  Optional<Movie> findMovieById(Integer id);
}
