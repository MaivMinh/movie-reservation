package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Room;
import com.foolish.moviereservation.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Integer> {
  public List<Seat> findAllByRoom(Room room);
}
