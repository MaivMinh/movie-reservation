package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.MovieDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.MovieMapperImpl;
import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.repository.MovieRepo;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.specifications.MovieSpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.foolish.moviereservation.specifications.MovieSpecs.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class MovieService {
  private final MovieRepo movieRepo;
  private final MovieMapperImpl movieMapperImpl;

  public Movie save(Movie movie) {
    return movieRepo.save(movie);
  }

  public Movie findMovieByIdOrElseThrow(Integer id) {
    Optional<Movie> result = movieRepo.findMovieById(id);
    return result.orElseThrow(() -> new ResourceNotFoundException("Movie not found", Map.of("id", String.valueOf(id))));
  }

  public Movie findMovieByPoster(String poster) {
    Optional<Movie> result = movieRepo.findMovieByPoster(poster);
    return result.orElse(null);
  }

  public Page<Movie> findByCriteria(Map<String, String> criteria, Pageable pageable) {
    /*
     * Map<>: {
     * name: String,
     * description: String,
     * release_date: +2024-10-10,
     * rating: +4.5,
     * vote_count: +100,
     * }
     *
     * */

    Specification<Movie> specification = where(null);
    if (StringUtils.hasText(criteria.get("name"))) {
      specification = specification.and(containsName(criteria.get("name")));
    }
    if (StringUtils.hasText(criteria.get("description"))) {
      specification = specification.and(containsDescription(criteria.get("description")));
    }
    if (StringUtils.hasText(criteria.get("release_date"))) {
      String value = String.valueOf(criteria.get("release_date"));
      char c = value.charAt(0);
      if (c == '+') {
        String d = value.substring(1);
        Date date = new Date(Long.parseLong(d));
        specification = specification.and(where(releaseDateAfter(date).or(releaseDateEqualTo(date))));
      } else if (c == '-') {
        String d = value.substring(1);
        Date date = new Date(Long.parseLong(d));
        specification = specification.and(where(releaseDateBefore(date).or(releaseDateEqualTo(date))));
      } else {
        Date date = new Date(Long.parseLong(value));
        specification = specification.and(releaseDateEqualTo(date));
      }
    }

    if (StringUtils.hasText(criteria.get("rating"))) {
      String value = String.valueOf(criteria.get("rating"));
      char c = value.charAt(0);
      if (c == '+') {
        double given = Double.parseDouble(value.substring(1));
        specification = specification.and(where(voteAverageGreaterThan(given).or(voteAverageEqualTo(given))));
      } else if (c == '-') {
        double given = Double.parseDouble(value.substring(1));
        specification = specification.and(where(voteAverageLessThan(given).or(voteAverageEqualTo(given))));
      } else {
        double given = Double.parseDouble(value);
        specification = specification.and(where(voteAverageEqualTo(given)));
      }
    }
    if (StringUtils.hasText(criteria.get("vote_count"))) {
      String value = String.valueOf(criteria.get("vote_count"));
      char c = value.charAt(0);
      if (c == '+') {
        int given = Integer.parseInt(value.substring(1));
        specification = specification.and(where(voteCountGreaterThan(given).or(voteCountEqualTo(given))));
      } else if (c == '-') {
        int given = Integer.parseInt(value.substring(1));
        specification = specification.and(where(voteCountLessThan(given).or(voteCountEqualTo(given))));
      } else {
        int given = Integer.parseInt(value);
        specification = specification.and(where(voteCountEqualTo(given)));
      }
    }
    return movieRepo.findAll(specification, pageable);
  }

  public Page<MovieDTO> findMovieDTOs(Pageable pageable) {
    Page<Movie> page = movieRepo.findAll(pageable);
    List<Movie> movies = page.getContent();
    return new PageImpl<>(movies.stream().map(movieMapperImpl::toDTO).toList(), pageable, page.getTotalElements());
  }
}
