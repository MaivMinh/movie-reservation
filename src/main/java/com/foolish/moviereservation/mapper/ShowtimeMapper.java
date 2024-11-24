package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.ShowtimeDTO;
import com.foolish.moviereservation.model.Showtime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MovieMapper.class, RoomMapper.class})
public interface ShowtimeMapper {
  ShowtimeDTO toDTO(Showtime showtime);
  Showtime toEntity(ShowtimeDTO showtimeDTO);
}
