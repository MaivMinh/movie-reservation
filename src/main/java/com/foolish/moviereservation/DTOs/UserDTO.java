package com.foolish.moviereservation.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foolish.moviereservation.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String username;
  private String email;
  private String avatar;
  private Role role;
}
