package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.ShowtimeDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.ShowtimeMapper;
import com.foolish.moviereservation.mapper.ShowtimeMapperImpl;
import com.foolish.moviereservation.model.Showtime;
import com.foolish.moviereservation.repository.ShowtimeRepo;
import com.foolish.moviereservation.specifications.MovieSpecs;
import com.foolish.moviereservation.specifications.ShowtimeSpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class ShowtimeService {
  private final ShowtimeRepo showtimeRepo;
  private final ShowtimeMapper showtimeMapper;


  public Showtime save(Showtime showtime) {
    return showtimeRepo.save(showtime);
  }

  public ShowtimeDTO getShowtimeDTOByIdOrElseThrow(Integer id) {
    Showtime showtime = showtimeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Showtime not found", Map.of("id", String.valueOf(id))));
    return showtimeMapper.toDTO(showtime);
  }

  public Page<ShowtimeDTO> getShowtimesByCriteria(Map<String, String> criteria, Pageable pageable) {
    Specification<Showtime> specification = where(null);
    if (StringUtils.hasText(criteria.get("movie_id"))) {
      specification = specification.and(ShowtimeSpecs.containsMovieId(Integer.parseInt(criteria.get("movie_id"))));
    }

    if (StringUtils.hasText(criteria.get("date"))) {
      specification = specification.and(ShowtimeSpecs.containsShowtimeDate(new Date(criteria.get("date"))));
    }
    Page<Showtime> showtimes = showtimeRepo.findAll(specification, pageable);

    PageImpl<ShowtimeDTO> result = new PageImpl<>(showtimes.stream().map(showtimeMapper::toDTO).toList(), pageable, showtimes.getContent().size());
    return result;
  }

  public Showtime getShowtimeByIdOrElseThrow(Integer id) {
    return showtimeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Showtime not found", Map.of("id", String.valueOf(id))));
  }

  public void delete(Showtime showtime) {
    showtimeRepo.delete(showtime);
  }
}