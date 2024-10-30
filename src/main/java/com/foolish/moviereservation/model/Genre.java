package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "genres")
public class Genre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "genre_id")
  private Integer id;
  private String name;

  @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = MovieGenre.class)
  private Set<MovieGenre> movieGenres;
}
