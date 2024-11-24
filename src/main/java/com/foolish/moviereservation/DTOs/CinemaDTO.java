package com.foolish.moviereservation.DTOs;

import com.foolish.moviereservation.model.Banner;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CinemaDTO {
  private Integer id;
  private String name;
  private String address;
  private ProvinceDTO province;
}
