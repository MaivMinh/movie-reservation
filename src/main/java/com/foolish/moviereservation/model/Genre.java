package com.foolish.moviereservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

  @JsonIgnore
  @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = MovieGenre.class)
  private List<MovieGenre> movieGenres;
}
