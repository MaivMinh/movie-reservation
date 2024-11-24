package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Showtime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepo extends JpaRepository<Showtime, Integer>, JpaSpecificationExecutor<Showtime> {

}
