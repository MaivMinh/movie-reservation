package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.MovieDTO;
import com.foolish.moviereservation.model.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
  MovieDTO toDTO(Movie movie);
}
