package com.foolish.moviereservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Showtimes")
public class Showtime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  private Date date;

  @Column(name = "start_time")
  private Time startTime;
  @Column(name = "end_time")
  private Time endTime;
}
