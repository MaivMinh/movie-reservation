package com.foolish.moviereservation.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MovieDTO {
  private Integer id;
  private String name;
  private String poster;
  private Date releaseDate;
  private Double voteAverage;
  private Integer voteCount;
}
