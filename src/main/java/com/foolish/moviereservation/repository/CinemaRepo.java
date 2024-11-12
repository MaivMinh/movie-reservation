package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepo extends JpaRepository<Cinema, Integer> {
}
