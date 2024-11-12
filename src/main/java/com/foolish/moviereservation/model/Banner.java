package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Banners")
public class Banner {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private String url;

  @ManyToOne(targetEntity = Cinema.class)
  @JoinColumn(name = "cinema_id")
  private Cinema cinema;
}
