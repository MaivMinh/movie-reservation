package com.foolish.moviereservation.DTOs;

import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
public class UserDTO {
  private Integer userId;
  private String username;
  private String phoneNumber;
  private String email;
  private Date birthDate;
  private String avatar;
}
