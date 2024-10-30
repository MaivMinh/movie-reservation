package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movie_id")
  private Integer id;

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

  @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = MovieGenre.class)
  private Set<MovieGenre> movieGenres;
}
