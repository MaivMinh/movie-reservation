package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CinemaRepo extends JpaRepository<Cinema, Integer>, JpaSpecificationExecutor<Cinema> {

}
