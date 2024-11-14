package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.CinemaDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.CinemaMapper;
import com.foolish.moviereservation.mapper.CinemaMapperImpl;
import com.foolish.moviereservation.model.Cinema;
import com.foolish.moviereservation.repository.CinemaRepo;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.specifications.CinemaSpecs;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.foolish.moviereservation.specifications.CinemaSpecs.*;

@Service
@AllArgsConstructor
public class CinemaService {

  private final CinemaRepo cinemaRepo;
  private final CinemaMapperImpl cinemaMapperImpl;

  public Cinema getCinemaByIdOrElseThrow(Integer id) {
    Optional<Cinema> cinema = cinemaRepo.findById(id);
    return cinema.orElseThrow(() -> new ResourceNotFoundException("Cinema not found", Map.of("cinema_id", String.valueOf(id))));
  }

  public Cinema save(@Valid Cinema cinema) {
    return cinemaRepo.save(cinema);
  }

  public Page<CinemaDTO> findCinemasByCriteria(@NotNull Map<String, String> criteria, Pageable pageable) {
    Specification<Cinema> specification = Specification.where(null);
    if (StringUtils.hasText(criteria.get("name"))) {
      specification = specification.and(containsName(criteria.get("name")));
    }
    if (StringUtils.hasText(criteria.get("address"))) {
      specification = specification.and(containsAddress(criteria.get("address")));
    }
    if (StringUtils.hasText(criteria.get("province_id"))) {
      specification = specification.and(hasProvinceId(Integer.parseInt(criteria.get("province_id"))));
    }
    Page<Cinema> result = cinemaRepo.findAll(specification, pageable);
    List<CinemaDTO> list = result.getContent().stream().map(cinemaMapperImpl::toDTO).toList();

    return new PageImpl<>(list, pageable, result.getTotalElements());
  }
}
