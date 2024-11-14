package com.foolish.moviereservation.DTOs;

import com.foolish.moviereservation.controller.RoomDTO;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.springframework.boot.context.properties.bind.Name;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class ShowtimeDTO {
  private Integer id;
  private MovieDTO movie;
  private RoomDTO room;
  private Date date;
  private Time startTime;
  private Time endTime;
}
