package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;
  
  @NotNull
  private String username;
  @NotNull
  private String password;
  @NotNull
  @Column(name = "phone_number")
  private String phoneNumber;
  @NotNull
  private String email;
  @NotNull
  @Column(name = "birth_date")
  private Date birthDate;
  private String avatar;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserRole> userRoles;
}
