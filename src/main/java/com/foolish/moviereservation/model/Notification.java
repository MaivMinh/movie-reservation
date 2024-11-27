package com.foolish.moviereservation.model;

import com.foolish.moviereservation.records.NotificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {
  @Id
  private Integer id;

  private String message;
  private Timestamp createdAt;
  @OneToOne(mappedBy = "notification")
  private Booking booking;
  private NotificationStatus status;
}
