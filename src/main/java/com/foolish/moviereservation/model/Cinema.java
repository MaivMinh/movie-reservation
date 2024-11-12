package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cinemas")
public class Cinema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  private String name;
  private String address;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = Province.class, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "province")
  private Province province;


}
