package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "avatar", target = "avatar")
  User toUser(UserDTO userDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "avatar", target = "avatar")
  UserDTO toDTO(User user);
}
