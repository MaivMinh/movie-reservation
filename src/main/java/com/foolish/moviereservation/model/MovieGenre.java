package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movie_genres")
public class MovieGenre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "genre_id")
  private Genre genre;
}
