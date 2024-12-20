package com.foolish.moviereservation.records;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UpdatedMovie {
  private String name;
  private String description;
  private String trailer;
  private Date releaseDate;
}
