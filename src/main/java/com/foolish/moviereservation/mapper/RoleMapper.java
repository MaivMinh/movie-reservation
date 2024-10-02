package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.RoleDTO;
import com.foolish.moviereservation.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoleMapper {
  @Mapping(source = "name", target = "name")
  Role toRole(RoleDTO roleDTO);

  @Mapping(source = "name", target = "name")
  RoleDTO toDTO(Role role);
}
