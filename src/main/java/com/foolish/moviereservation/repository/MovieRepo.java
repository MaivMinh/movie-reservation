package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
  Optional<Movie> findMovieById(Integer id);
  Optional<Movie> findMovieByPoster(String poster);
  Page<Movie> findAll(Pageable pageable);
}
