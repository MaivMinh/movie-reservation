package com.foolish.moviereservation.model;

import com.foolish.moviereservation.records.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne(mappedBy = "payment")
  private Booking booking;
  private Double amount;
  private Timestamp createdAt;
  private String paymentMethod;
  private PaymentStatus status;
  private String transactionId;
}
