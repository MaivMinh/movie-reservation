package com.foolish.moviereservation.model;

import com.foolish.moviereservation.records.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Bookings")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "showtime_id")
  private Showtime showtime;

  private Double totalPrice;
  private Timestamp bookingTime;

  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  @OneToOne(targetEntity = Notification.class)
  private Notification notification;

  @OneToOne(targetEntity = Payment.class)
  private Payment payment;

}
