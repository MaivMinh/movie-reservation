package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.UserDTO;
import com.foolish.moviereservation.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "avatar", target = "avatar")
  @Mapping(source = "role", target = "role")
  User toUser(UserDTO userDTO);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "avatar", target = "avatar")
  @Mapping(source = "role", target = "role")
  UserDTO toDTO(User user);
}
