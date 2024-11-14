package com.foolish.moviereservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "province")
  private Province province;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Banner.class, orphanRemoval = true)
  @JoinColumn(name = "cinema_id")
  private List<Banner> banners;
}
