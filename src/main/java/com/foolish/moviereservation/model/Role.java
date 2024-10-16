package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer roleId;
  @NotNull
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
  private List<UserRole> userRoles;

  public static final Integer ADMIN = 1;
  public static final Integer USER = 2;
}
