package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.SeatDTO;
import com.foolish.moviereservation.model.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface SeatMapper {

  @Mapping(source = "room.id", target = "roomId")
  SeatDTO toDTO(Seat seat);

  @Mapping(source = "roomId", target = "room.id")
  Seat toEntity(SeatDTO seatDTO);
}
