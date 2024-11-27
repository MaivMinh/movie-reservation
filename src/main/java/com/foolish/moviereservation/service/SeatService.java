package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.SeatDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.SeatMapperImpl;
import com.foolish.moviereservation.model.Room;
import com.foolish.moviereservation.model.Seat;
import com.foolish.moviereservation.repository.SeatRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SeatService {

  private final SeatRepo seatRepo;
  private final RoomService roomService;
  private final SeatMapperImpl seatMapperImpl;

  public Seat save(Seat seat) {
    return seatRepo.save(seat);
  }

  public Seat getSeatByIdOrElseThrow(Integer id) {
    return seatRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + id, Map.of("id", String.valueOf(id))));
  }

  public void delete(Seat seat) {
    seatRepo.delete(seat);
  }

  public List<SeatDTO> findAllByRoomId(Integer roomId) {
    Room room = roomService.getRoomByIdOrElseThrow(roomId);
    List<Seat> seats = seatRepo.findAllByRoom(room);
    return seats.stream().map(seatMapperImpl::toDTO).toList();
  }
}
