package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "Movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer movieId;

  @NotNull
  private String name;
  @NotNull
  private String poster;
  @NotNull
  private String description;
  private String trailer;
  @NotNull
  private Date releaseDate;
  private Double voteAverage;
  private Integer voteCount;
}
