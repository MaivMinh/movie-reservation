package com.foolish.moviereservation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "provinces")
public class Province {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "province", targetEntity = Cinema.class)
  private List<Cinema> cinemas;
}
