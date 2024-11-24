package com.foolish.moviereservation.specifications;

import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.model.Showtime;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;


public class ShowtimeSpecs {
  public static Specification<Showtime> containsMovieId(Integer id) {
    return (root, query, cb) -> {
      final Path<Movie> movie = root.get("movie");
      return cb.equal(movie.get("id"), id);
    };
  }

  public static  Specification<Showtime> containsShowtimeDate(Date date) {
    return (root, query, cb) -> {
      final Path<Date> d = root.get("date");
      return cb.equal(d, date);
    };
  }
}
