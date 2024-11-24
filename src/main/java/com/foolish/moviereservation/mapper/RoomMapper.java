package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.controller.RoomDTO;
import com.foolish.moviereservation.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CinemaMapperImpl.class})
public interface RoomMapper {
  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "location", target = "location")
  RoomDTO toDTO(Room room);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "location", target = "location")
  Room toEntity(RoomDTO roomDTO);
}
