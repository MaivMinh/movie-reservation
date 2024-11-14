package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.CinemaDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
  private Integer id;
  private String name;
  private String location;
  private CinemaDTO cinema;
}
