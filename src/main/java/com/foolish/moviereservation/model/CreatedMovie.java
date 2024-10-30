package com.foolish.moviereservation.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

public class CreatedMovie {
  private String name;
  private MultipartFile file;
  @NotNull
  private String description;
  private String trailer;
  @NotNull
  private Date releaseDate;
}
