package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Users")
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer id;
  
  @NotNull
  private String username;
  @NotNull
  private String password;
  @NotNull
  private String email;

  @NotNull
  @Column(name = "name")
  private String name;
  private String avatar;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;
}
