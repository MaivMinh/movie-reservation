package com.foolish.moviereservation.DTOs;

import com.foolish.moviereservation.records.SeatStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDTO {
  private Integer id;
  private String type;
  private Double price;
  private String seatRow;
  private Integer seatNumber;
  private SeatStatus status;
  private Integer roomId;
}
