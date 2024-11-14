package com.foolish.moviereservation.service;

import com.foolish.moviereservation.controller.RoomDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.RoomMapperImpl;
import com.foolish.moviereservation.model.Room;
import com.foolish.moviereservation.repository.RoomRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
  private final RoomRepo roomRepo;
  private final RoomMapperImpl roomMapperImpl;

  public Room getRoomById(Integer id) {
    return roomRepo.findById(id).orElse(null);
  }

  public RoomDTO getRoomDTOByIdOrElseThrow(Integer id) {
    Room room = roomRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room id not found", Map.of("room_id", String.valueOf(id))));
    return roomMapperImpl.toDTO(room);
  }

  public Room save(Room room) {
    return roomRepo.save(room);
  }

  public Room getRoomByIdOrElseThrow(Integer id) {
    Optional<Room> room = roomRepo.findById(id);
    return room.orElseThrow(() -> new ResourceNotFoundException("Room id not found", Map.of("room_id", String.valueOf(id))));
  }
}
