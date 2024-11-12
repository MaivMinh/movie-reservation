package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
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

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Banner.class)
  private List<Banner> banners;
}
